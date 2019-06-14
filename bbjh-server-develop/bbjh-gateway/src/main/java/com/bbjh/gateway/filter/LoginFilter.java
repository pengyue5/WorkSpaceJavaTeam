package com.bbjh.gateway.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.bbjh.common.api.ResponseMessage;
import com.bbjh.common.constant.CacheConstant;

import com.bbjh.common.dto.RightInfoDTO;
import com.bbjh.common.enums.ResultCode;
import com.bbjh.common.utils.BeanFactoryUtil;
import com.bbjh.common.utils.ResponseUtil;
import com.bbjh.gateway.dto.AdminLoginInfoDTO;
import com.bbjh.gateway.dto.UserLoginInfoDTO;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author fangwenbo
 * @Description 根据sessionId进行登录信息的标记
 * @date 2019/4/17
 */
@Slf4j
@Component
public class LoginFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 40;
    }

    @Override
    public boolean shouldFilter() {
        if (HttpMethod.OPTIONS.name().equals(RequestContext.getCurrentContext().getRequest().getMethod())) {
            //对预检请求不拦截
            return false;
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        Object whiteList = ctx.get("whiteList");
        if (whiteList.equals(true)) {
            return false;
        }
        HttpServletRequest request = ctx.getRequest();
        Object noHandler = ctx.get("noHandler");
        if (noHandler.equals(true)) {
            String msg = StrUtil.format("{} 未初始化", request.getServletPath());
            log.warn(msg);
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.OK.value());
            ctx.setResponseBody(JSONObject.toJSONString(ResponseMessage.error(ResultCode.RIGHT_NOT_INITIALISE)));
            ResponseUtil.allCors(request, ctx.getResponse());
            return false;
        }

        Object requireAuth = ctx.get("requireAuth");
        //对需要授权的请求进行过滤，不需要授权的不过滤直接转发
        return requireAuth.equals(true);
    }


    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        try {
            String sessionId = request.getHeader("sessionId");
            RedisTemplate redisTemplate =
                    (RedisTemplate) BeanFactoryUtil.getBean("redisTemplate");

            if (StrUtil.isEmpty(sessionId)) {
                log.warn("缺少sessionId");
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(HttpStatus.OK.value());
                ctx.setResponseBody(JSONObject.toJSONString(ResponseMessage.error(ResultCode.NO_SESSION)));
                ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

                HttpServletResponse response = ctx.getResponse();
                ResponseUtil.allCors(ctx.getRequest(), response);

                return null;
            } else {
                AdminLoginInfoDTO admin = null;
                UserLoginInfoDTO user = null;

                Object o =
                        redisTemplate.opsForValue().get(CacheConstant.NAMESPACE_ADMIN_LOGIN + sessionId);
                if (ObjectUtil.isNotNull(o)) {
                    //转发时向header中传递当前用户的类型及sessionId
                    ctx.addZuulRequestHeader("loginType", "admin");
                    ctx.addZuulRequestHeader("sessionId", CacheConstant.NAMESPACE_ADMIN_LOGIN + sessionId);
                    admin = (AdminLoginInfoDTO) o;
                }

                Object o2 =
                        redisTemplate.opsForValue().get(CacheConstant.NAMESPACE_USER_LOGIN + sessionId);
                if (ObjectUtil.isNotNull(o2)) {
                    //转发时向header中传递当前用户的类型及sessionId
                    ctx.addZuulRequestHeader("loginType", "user");
                    ctx.addZuulRequestHeader("sessionId", CacheConstant.NAMESPACE_USER_LOGIN + sessionId);
                    user = (UserLoginInfoDTO) o2;
                }

                if (ObjectUtil.isNull(admin) && ObjectUtil.isNull(user)) {
                    String msg = StrUtil.format("session : {} 已失效", sessionId);
                    log.warn(msg);
                    ctx.setSendZuulResponse(false);
                    ctx.setResponseStatusCode(HttpStatus.OK.value());
                    ctx.setResponseBody(JSONObject.toJSONString(ResponseMessage.error(ResultCode.SESSION_EXPIRED)));
                    ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    ResponseUtil.allCors(request, ctx.getResponse());
                    return null;
                } else {
                    Long expire = 0L;
                    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

                    if (ObjectUtil.isNotNull(admin)) {
                        expire = CacheConstant.EXPIRE_ADMIN_LOGIN;
                        requestAttributes.setAttribute(CacheConstant.NAMESPACE_ADMIN_LOGIN, admin,
                                RequestAttributes.SCOPE_SESSION);
                        //session保活
                        redisTemplate.opsForValue().set(CacheConstant.NAMESPACE_ADMIN_LOGIN + sessionId, admin, expire,
                                TimeUnit.SECONDS);
                    } else if (ObjectUtil.isNotNull(user)) {
                        expire = CacheConstant.EXPIRE_USER_LOGIN;
                        requestAttributes.setAttribute(CacheConstant.NAMESPACE_USER_LOGIN, admin,
                                RequestAttributes.SCOPE_REQUEST);
                        //session保活
                        redisTemplate.opsForValue().set(CacheConstant.NAMESPACE_USER_LOGIN + sessionId, user, expire,
                                TimeUnit.SECONDS);
                    } else {
                        String msg = StrUtil.format("无效的sessionId:{}", sessionId);
                        log.warn(msg);
                        ctx.setSendZuulResponse(false);
                        ctx.setResponseStatusCode(HttpStatus.OK.value());
                        ctx.setResponseBody(JSONObject.toJSONString(ResponseMessage.error(ResultCode.INVALID_SESSION)));
                        ResponseUtil.allCors(request, ctx.getResponse());
                        return null;
                    }
                    RequestContextHolder.getRequestAttributes().setAttribute("sessionId", sessionId, RequestAttributes.SCOPE_REQUEST);
                    if (ObjectUtil.isNotNull(user)) {
                        //App用户放行,不校验权限
                        return null;
                    }

                    Set<Boolean> hasRight = new HashSet<>();
                    if (ObjectUtil.isNotNull(admin)) {
                        //权限校验
                        hasRight(admin.getRights(), request.getServletPath(), hasRight);
                    }

                    if (!hasRight.contains(true)) {
                        log.warn("没有权限");
                        ctx.setSendZuulResponse(false);
                        ctx.setResponseStatusCode(HttpStatus.OK.value());
                        ctx.setResponseBody(JSONObject.toJSONString(ResponseMessage.error(ResultCode.NO_PERMISSION)));
                        ResponseUtil.allCors(request, ctx.getResponse());
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            log.error("LoginCheckFilter出现异常", e);
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.BAD_REQUEST.value());
            ctx.setResponseBody(JSONObject.toJSONString(ResponseMessage.error(ResultCode.SYSTEM_BUSY)));
            ResponseUtil.allCors(request, ctx.getResponse());
            return null;
        }
        return null;
    }

    private void hasRight(List<RightInfoDTO> rights, String servletPath, Set<Boolean> hasRight) {
        if (ObjectUtil.isNotNull(rights)) {
            for (RightInfoDTO right : rights) {
                hasRight(right.getChildren(), servletPath, hasRight);
                //如果自己用户的权限中有和当前请求可以匹配，则拥有权限
                if (right.getRightUrl().equals(servletPath)) {
                    hasRight.add(true);
                    break;
                }
            }
        }
    }

}
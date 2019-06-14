package com.bbjh.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.bbjh.common.api.ResponseMessage;
import com.bbjh.common.constant.CacheConstant;

import com.bbjh.common.dto.RightInfoDTO;
import com.bbjh.common.utils.BeanFactoryUtil;
import com.bbjh.common.utils.ResponseUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author fangwenbo
 * @Description 根据权限表中的记录对当前请求进行标记，标记是否需要授权才能访问
 * @date 2019/4/17
 */
@Slf4j
@Component
public class AuthFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 30;
    }

    @Override
    public boolean shouldFilter() {
        if (HttpMethod.OPTIONS.name().equals(RequestContext.getCurrentContext().getRequest().getMethod())) {
            //对预检请求不拦截
            return false;
        }
        Object whiteList = RequestContext.getCurrentContext().get("whiteList");
        //不在白名单中的请求进行拦截
        return !whiteList.equals(true);
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        boolean requireAuth = true;
        boolean noHandler = true;
        RedisTemplate redisTemplate;
        try {
            try {
                redisTemplate =
                        (RedisTemplate) BeanFactoryUtil.getBean(
                                "redisTemplate");
            } catch (Exception e) {
                log.error("获取redisTemplate出现异常", e);
                //不进行路由
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(HttpStatus.BAD_REQUEST.value());
                ctx.setResponseBody(JSONObject.toJSONString(ResponseMessage.error()));
                ResponseUtil.allCors(request, ctx.getResponse());
                return null;
            }

            Map<Integer, RightInfoDTO> entries = redisTemplate.opsForHash().entries(CacheConstant.KEY_ALL_RIGHT);
            for (Map.Entry<Integer, RightInfoDTO> entry : entries.entrySet()) {
                 String rightUrl = entry.getValue().getRightUrl();
                String servletPath = request.getServletPath();
                if (rightUrl.equals(servletPath)) {
                    noHandler = false;
                }
                //对当前请求路径和权限表进行匹配
                if (rightUrl.equals(servletPath) && new Byte((byte)0).equals(entry.getValue().getRequireAuth())) {
                    requireAuth = false;
                    break;
                }
            }
            //对不需要授权的请求添加requireAuth标志
            ctx.set("requireAuth", requireAuth);

            //对权限表中还没有配置的权限进行标记
            ctx.set("noHandler", noHandler);
        } catch (Exception e) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(HttpStatus.BAD_REQUEST.value());
            ctx.setResponseBody(JSONObject.toJSONString(ResponseMessage.error()));
            ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            ResponseUtil.allCors(request, ctx.getResponse());
            return null;
        }
        return null;
    }
}

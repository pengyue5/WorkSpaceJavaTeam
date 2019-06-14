package com.bbjh.gateway.filter;

import com.bbjh.common.utils.ResponseUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author fangwenbo
 * @Description 预检请求放行过滤器
 * @date 2019/4/19
 */
@Slf4j
@Component
public class OptionFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
            //对预检请求进行过滤
        return HttpMethod.OPTIONS.name().equals(RequestContext.getCurrentContext().getRequest().getMethod());
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();

        //不再路由
        ctx.setSendZuulResponse(false);

        ctx.setResponseStatusCode(HttpStatus.OK.value());
        ResponseUtil.allCors(ctx.getRequest(), ctx.getResponse());

        return false;
    }
}

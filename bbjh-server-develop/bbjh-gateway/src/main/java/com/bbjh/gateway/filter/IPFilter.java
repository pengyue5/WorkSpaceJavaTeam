package com.bbjh.gateway.filter;

import cn.hutool.extra.servlet.ServletUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fangwenbo
 * @Description
 * @date 2019/4/19
 */
@Slf4j
@Component
public class IPFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        String clientIP = ServletUtil.getClientIP(request);
        if (clientIP.equals("127.0.0.1")) {
            currentContext.set("innerNet", true);
        }
        return null;
    }
}

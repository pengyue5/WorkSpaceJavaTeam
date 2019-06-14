package com.bbjh.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author fangwenbo
 * @Description 不需要登录和鉴权的请求路径
 * @date 2019/4/19
 */
@Slf4j
@Component
public class WhiteListFilter extends ZuulFilter {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 不需要登录和鉴权的路径前缀
     */
    private static final List<String> WHITE_LIST_PREFIX = Arrays.asList("/open/**", "/**/api-docs", "/**/api-docs-ext");

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 20;
    }

    @Override
    public boolean shouldFilter() {
        //对非预检请求过滤
        return !HttpMethod.OPTIONS.name().equals(RequestContext.getCurrentContext().getRequest().getMethod());
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
        //是否是白名单中请求
        ctx.set("whiteList", false);
        WHITE_LIST_PREFIX.forEach(pattern -> {
            if (antPathMatcher.match(pattern, uri)) {
                ctx.set("whiteList", true);
            }
        });
        return null;
    }
}

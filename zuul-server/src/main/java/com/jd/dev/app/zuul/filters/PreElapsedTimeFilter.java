package com.jd.dev.app.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreElapsedTimeFilter extends ZuulFilter {
    private static final String PRE_FILTER = "pre";
    private static final int FILTER_ORDER_1 = 1;
    private static final Logger log = LoggerFactory.getLogger(PreElapsedTimeFilter.class);

    @Override
    public String filterType() {
        return PRE_FILTER;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER_1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s request routing to %s", request.getMethod(), request.getRequestURL().toString()));

        long initTime = System.currentTimeMillis();
        request.setAttribute("initTime", initTime);
        return null;
    }
}

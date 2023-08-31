package com.jd.dev.app.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostElapsedTimeFilter extends ZuulFilter {
    private static final String POST_FILTER = "post";
    private static final int FILTER_ORDER_1 = 1;
    private static final Logger log = LoggerFactory.getLogger(PostElapsedTimeFilter.class);

    @Override
    public String filterType() {
        return POST_FILTER;
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
        log.info("Begin post filter");

        Long startTime = (long) request.getAttribute("initTime");
        Long finishTime = System.currentTimeMillis();
        Long elapsedTime = finishTime - startTime;
        log.info(String.format("Elapsed time seconds %s seg", elapsedTime.doubleValue() / 1000.00));
        log.info(String.format("Elapsed time in milliseconds  %s ms", elapsedTime));

        return null;
    }
}

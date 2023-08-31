package com.jd.dev.app.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ExampleGlobalFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(ExampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Pre Filter");
        long initTime = System.currentTimeMillis();

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long finishTime = System.currentTimeMillis();
            Long elapsedTime = finishTime - initTime;
            logger.info("Post Filter");
            logger.info(String.format("Elapsed time seconds %s seg", elapsedTime.doubleValue() / 1000.00));
            logger.info(String.format("Elapsed time in milliseconds  %s ms", elapsedTime));
        }));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

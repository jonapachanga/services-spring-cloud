package com.jd.dev.app.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SpringSecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SpringSecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        String[] publicPaths = { "/api/security/oauth/**" };
        return http.authorizeExchange()
                .pathMatchers(publicPaths).permitAll()
                .pathMatchers(HttpMethod.GET, "/api/items", "/api/products", "/api/users/users").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/items/**", "/api/products/**", "/api/users/**").hasRole("ADMIN")
                .anyExchange()
                .authenticated()
                .and()
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .build();
    }
}

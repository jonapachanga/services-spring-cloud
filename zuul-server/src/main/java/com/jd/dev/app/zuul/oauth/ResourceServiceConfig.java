package com.jd.dev.app.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServiceConfig extends ResourceServerConfigurerAdapter {
    @Value("${config.security.oauth.jwt.key}")
    private String jwtKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Aqui es donde se protegen los endpoints, definiendo tipos de accesos
        http.authorizeRequests()
                .antMatchers("/api/security/oauth/token/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/items", "/api/products")
                .permitAll()
                .antMatchers("/api/items/**", "/api/products/**", "/api/users/**").hasRole("ADMIN")
                .and()
                .authorizeRequests().anyRequest()
                .authenticated() // se indica que cualquier otra ruta requiere de autenticacion
                .and()
                .cors().configurationSource(CorsConfigurationSource()); // habilitacion Cors
    }

    @Bean
    public CorsConfigurationSource CorsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("*"));
        corsConfig.setAllowedMethods(Arrays.asList(HttpMethod.POST.toString(),
                HttpMethod.GET.toString(),
                HttpMethod.PUT.toString(),
                HttpMethod.PATCH.toString(),
                HttpMethod.DELETE.toString(),
                HttpMethod.OPTIONS.toString()));
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    // Se configura filtro para que se aplique en forma global, no solo spring security sino en toda la
    // aplicacion, solo si las aplicaciones residen en otro dominio, para que el intercambio de recurso
    // no sea bloqueado por Cors de origen cruzado
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(
                new CorsFilter(CorsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
        // Aqui se carga o utiliza el codigo secreto para la FIRMA del token
        // y el mismo se utiliza luego para validar el token en el servidor de recursos
        tokenConverter.setSigningKey(Base64.getEncoder().encodeToString(jwtKey.getBytes()));

        return tokenConverter;
    }

}

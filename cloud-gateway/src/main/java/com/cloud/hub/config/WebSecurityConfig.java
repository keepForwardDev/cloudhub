package com.cloud.hub.config;

import com.cloud.hub.service.login.impl.ReactiveLogoutHandler;
import com.cloud.hub.service.login.impl.UserSessionSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.web.server.authentication.OAuth2LoginAuthenticationWebFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author jaxMine
 * @date 2020/4/3
 */
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveLogoutHandler reactiveLogoutHandler) {
        UserSessionSecurityContextRepository repository = new UserSessionSecurityContextRepository();
        http.csrf().disable().oauth2Login().and()
                // 主要为了清除认证服务器的token
                .logout().logoutHandler(reactiveLogoutHandler).and()
                // 主要为保存全局UserAuthentication
                .securityContextRepository(repository)
                .httpBasic().disable().authorizeExchange().anyExchange().permitAll();
        SecurityWebFilterChain chain = http.build();
        // 自动装配居然使用的securityContextRepository居然不是同一个，将坑补上
        chain.getWebFilters().filter(r -> r instanceof OAuth2LoginAuthenticationWebFilter)
                .cast(OAuth2LoginAuthenticationWebFilter.class)
                .doOnNext(oAuth2LoginAuthenticationWebFilter -> {
                    oAuth2LoginAuthenticationWebFilter.setSecurityContextRepository(repository);
                }).subscribe();
        return chain;
    }

}

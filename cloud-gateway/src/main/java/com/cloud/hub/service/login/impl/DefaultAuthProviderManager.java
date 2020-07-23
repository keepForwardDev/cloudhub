package com.cloud.hub.service.login.impl;

import com.cloud.hub.service.login.AuthenticationProvider;
import com.cloud.hub.service.login.ReactiveSecurityContextRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DefaultAuthProviderManager implements AuthenticationManager, ReactiveSecurityContextRepository, InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private List<AuthenticationProvider> providers = new ArrayList<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication userAuthentication = (UserAuthentication) authentication;
        for (AuthenticationProvider provider : getProviders()) {
            if (!provider.supports(userAuthentication.getLoginType())) {
                continue;
            }
            Authentication auth = provider.authenticate(authentication);
            if (auth != null) {
                return auth;
            }
        }
        return null;
    }

    public List<AuthenticationProvider> getProviders() {
        return providers;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, AuthenticationProvider> authenticationProvider = applicationContext.getBeansOfType(AuthenticationProvider.class);
        authenticationProvider.forEach((k, v) -> {
            getProviders().add(v);
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Mono<SecurityContext> loadContext(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getSession().map(session -> {
            SecurityContext context = (SecurityContext) session.getAttributes().get(WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);
            return context;
        });
    }

    @Override
    public void saveContext(SecurityContext context, ServerWebExchange serverWebExchange) {
        serverWebExchange.getSession().doOnNext(session -> {
            session.getAttributes().put(WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME, context);
        }).subscribe();
    }

    @Override
    public Mono<Boolean> containsContext(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getSession().map(session -> {
            SecurityContext context = (SecurityContext) session.getAttributes().get(WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);
            return context != null;
        });
    }
}

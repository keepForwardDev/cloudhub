package com.cloud.hub.service.login.impl;

import com.cloud.hub.service.feign.Oauth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReactiveLogoutHandler extends SecurityContextServerLogoutHandler {

    @Autowired
    private Oauth2Service oauth2Service;

    @Override
    public Mono<Void> logout(WebFilterExchange exchange,
                             Authentication authentication) {
        UserAuthentication userAuthentication = (UserAuthentication) authentication;
        oauth2Service.deleteToken(userAuthentication.getToken());
        return super.logout(exchange, authentication);
    }
}

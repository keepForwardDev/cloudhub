package com.cloud.hub.service.login.impl;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;

public class UserSessionSecurityContextRepository extends WebSessionServerSecurityContextRepository {

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        OAuth2AuthenticationToken auth2AuthenticationToken = (OAuth2AuthenticationToken) context.getAuthentication();
        DefaultOAuth2User user = (DefaultOAuth2User) auth2AuthenticationToken.getPrincipal();
        LinkedHashMap<String,Object> details = (LinkedHashMap<String, Object>) user.getAttributes().get("details");
        Object tokenValue = details.get("tokenValue");
        // 此处保存的是uuid
        String uuid = user.getAttributes().get("name").toString();
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setUsername(uuid);
        userAuthentication.setToken(tokenValue.toString());
        context.setAuthentication(userAuthentication);
        return super.save(exchange, context);
    }
}

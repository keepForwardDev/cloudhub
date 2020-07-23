package com.cloud.hub.service.login;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 保存session SecurityContext
 */
public interface ReactiveSecurityContextRepository {

    Mono<SecurityContext> loadContext(ServerWebExchange serverWebExchange);


    void saveContext(SecurityContext context, ServerWebExchange serverWebExchange);

    Mono<Boolean> containsContext(ServerWebExchange serverWebExchange);
}

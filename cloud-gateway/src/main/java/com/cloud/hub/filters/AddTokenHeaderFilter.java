package com.cloud.hub.filters;

import com.cloud.hub.consts.LoginConst;
import com.cloud.hub.service.login.impl.UserAuthentication;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 当用户单点登录成功后 后面的请求自动带上token
 *
 */
@Component
public class AddTokenHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getSession().flatMap(session -> {
            SecurityContext securityContext = session.getAttribute(WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME);
            if (securityContext == null) {
                return chain.filter(exchange);
            }
            UserAuthentication oAuth2AuthenticationToken = (UserAuthentication) securityContext.getAuthentication();
            String tokenValue = oAuth2AuthenticationToken.getToken();
            String accessToken = exchange.getRequest().getQueryParams().getFirst(LoginConst.ACCESS_TOKEN);
            String bearerHeader = exchange.getRequest().getHeaders().getFirst(LoginConst.BEARER_TYPE);
            // url或header中可以传入token，主动传的以调用token为准
            if (tokenValue != null && StringUtils.isBlank(accessToken) && StringUtils.isBlank(bearerHeader)) {
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("Authorization", "Bearer " + tokenValue.toString())
                        .build();
                return chain.filter(exchange.mutate().request(request).build());
            } else {
                return chain.filter(exchange);
            }
        });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

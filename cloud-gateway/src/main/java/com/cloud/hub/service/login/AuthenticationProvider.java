package com.cloud.hub.service.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationProvider {
    //通过参数Authentication对象，进行认证
    Authentication authenticate(Authentication authentication)
            throws AuthenticationException;

    //是否支持该认证类型
    boolean supports(String type);
}

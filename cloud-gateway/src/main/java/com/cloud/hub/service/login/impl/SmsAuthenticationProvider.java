package com.cloud.hub.service.login.impl;

import com.cloud.hub.consts.LoginConst;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class SmsAuthenticationProvider extends UsernamePasswordAuthenticationProvider {

    @Value("system.default-password")
    private String superPassword;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication userAuthentication = (UserAuthentication) authentication;
        String code = stringRedisTemplate.opsForValue().get(LoginConst.REDIS_CODE_LOGIN_PREFFIX + userAuthentication.getUsername());
        userAuthentication.setPassword(superPassword);
        if (StringUtils.isNotBlank(userAuthentication.getCode()) && userAuthentication.getCode().equals(code)) {
            super.authenticate(authentication);
        } else {
            throw new BadCredentialsException("验证码错误！");
        }
        return null;
    }

    @Override
    public boolean supports(String type) {
        return LoginConst.SMS_WAY.equals(type);
    }
}

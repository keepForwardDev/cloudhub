package com.cloud.hub.service.login.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.hub.consts.LoginConst;
import com.cloud.hub.service.login.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * 用户密码验证登录，
 */
@Service
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ReactiveClientRegistrationRepository registrationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${system.auth-server}")
    private String oauth2Server;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAuthentication userAuthentication = (UserAuthentication) authentication;
        ClientRegistration clientRegistration = registrationRepository.findByRegistrationId(LoginConst.DEFAULT_CLIENT_KEY).block();
        // oauth2 token 获取地址
        String url = "http://" + oauth2Server + "/auth/oauth/token";
        MultiValueMap<String, String> params = getParams(clientRegistration, userAuthentication);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> paramsMap = new HttpEntity<MultiValueMap<String, String>>(params, headers);
        // 认证服务器获取token
        String content = null;
        try {
            content = restTemplate.postForObject(url, paramsMap,String.class);
        } catch (HttpClientErrorException.BadRequest ex) {
            content = ex.getResponseBodyAsString(StandardCharsets.UTF_8);
        }
        JSONObject obj = JSONObject.parseObject(content);
        Object accessToken = obj.get("access_token");
        Object error = obj.get("error_description");
        if (accessToken != null) {
            // clear password
            userAuthentication.setPassword(null);
            userAuthentication.setToken(accessToken.toString());
            return userAuthentication;
        } else {
            throw new BadCredentialsException(error.toString());
        }
    }

    @Override
    public boolean supports(String type) {
        return LoginConst.PASSWORD_WAY.equals(type);
    }

    private MultiValueMap<String, String> getParams(ClientRegistration registration, UserAuthentication authentication) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", registration.getClientId());
        params.add("client_secret", registration.getClientSecret());
        // 使用密码登录方式
        params.add("grant_type", "password");
        params.add("username", authentication.getUsername());
        params.add("password", authentication.getPassword());
        return params;
    }
}

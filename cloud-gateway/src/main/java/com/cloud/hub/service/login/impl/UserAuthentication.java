package com.cloud.hub.service.login.impl;

import com.cloud.hub.consts.LoginConst;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 网关用户只保存token相关信息
 */
public class UserAuthentication implements Authentication {

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码（包括手机）
     */
    private String code;

    private String loginType = LoginConst.PASSWORD_WAY;

    /**
     * 默认调用成功创建
     */
    private boolean authenticated = true;

    private String token;

    /**
     * 配置里面的 registration.key
     */
    private String clientKey = LoginConst.DEFAULT_CLIENT_KEY;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }
}

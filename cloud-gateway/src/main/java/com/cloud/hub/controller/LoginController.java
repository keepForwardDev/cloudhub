package com.cloud.hub.controller;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.service.login.impl.DefaultAuthProviderManager;
import com.cloud.hub.service.login.impl.UserAuthentication;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ServerWebExchange;

@RestController
public class LoginController {
    @Autowired
    private DefaultAuthProviderManager providerManager;

    /**
     * 登录流程，使用密码授权方式，去认证服务器获取token
     * @param type 登录的方式 {@link com.cloud.hub.consts.LoginConst }
     * @param username 用户名，可以为手机号，登录名
     * @param password
     * @param code 验证码
     * @param serverWebExchange
     * @return
     */
    @RequestMapping(value = "/login/{type}", method = RequestMethod.POST)
    public ResponseResult loginByPassword(@PathVariable(value = "type") String type, String username, String password, String code, ServerWebExchange serverWebExchange) {
        UserAuthentication userAuthentication = new UserAuthentication();
        userAuthentication.setPassword(password);
        userAuthentication.setUsername(username);
        userAuthentication.setCode(code);
        userAuthentication.setLoginType(type);
        String token = null;
        try {
            providerManager.authenticate(userAuthentication);
            token = userAuthentication.getToken();
            SecurityContext context = new SecurityContextImpl();
            context.setAuthentication(userAuthentication);
            providerManager.saveContext(context, serverWebExchange);
        } catch (AuthenticationException ex) {
            return new ResponseResult(-1, ex.getMessage());
        } catch (ResourceAccessException ex) {
            return new ResponseResult(-1, "认证服务器请求失败！");
        }
        return new ResponseResult(1, StringUtils.defaultString(userAuthentication.getToken(), StringUtils.EMPTY));
    }
}

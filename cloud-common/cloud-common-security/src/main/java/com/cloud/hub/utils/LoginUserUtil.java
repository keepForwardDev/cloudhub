package com.cloud.hub.utils;

import com.cloud.hub.bean.ShiroUser;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUserUtil {

    public static ShiroUser getCurrentUser() {
        Authentication authentication =  getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (AnonymousAuthenticationToken.class.equals(authentication.getClass())) {
            return null;
        }
        ShiroUser copyUser = new ShiroUser();
        BeanUtils.copyProperties(authentication.getPrincipal(), copyUser);
        return copyUser;
    }

    public static Authentication getAuthentication() {
        return getSecurityContext().getAuthentication();
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }
}

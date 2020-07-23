package com.cloud.hub.utils;

import javax.servlet.http.HttpServletRequest;

public class WebUtils extends org.springframework.web.util.WebUtils {

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}

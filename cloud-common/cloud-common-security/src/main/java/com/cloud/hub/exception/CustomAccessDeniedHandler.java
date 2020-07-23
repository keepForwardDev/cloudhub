package com.cloud.hub.exception;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.consts.ResponseConst;
import com.cloud.hub.utils.JsonUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录的用户访问没有权限访问资源的异常
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        boolean isAjax = request.getHeader("x-requested-with") != null;
        response.setCharacterEncoding("UTF-8");
        ResponseResult responseResult = new ResponseResult(ResponseConst.CODE_ERROR, ResponseConst.MSG_DENY_MESSAGE);
        if (isAjax) {
            response.setContentType("application/json");
        } else {
            response.setContentType("text/html;charset=utf-8");
        }
        response.getWriter().println(JsonUtils.toJsonString(responseResult));
    }
}

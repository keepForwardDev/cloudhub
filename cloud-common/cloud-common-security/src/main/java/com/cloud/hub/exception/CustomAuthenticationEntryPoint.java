package com.cloud.hub.exception;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.consts.ResponseConst;
import com.cloud.hub.utils.JsonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * anonymous 或者记住用户 未经认证 访问资源抛出的异常处理
 * @author jaxMine
 * @date 2020/4/2
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        boolean isAjax = request.getHeader("x-requested-with") != null;
        response.setCharacterEncoding("UTF-8");
        ResponseResult responseResult = new ResponseResult();

        if(authException.getCause() instanceof InvalidTokenException){
            // 无效token
            InvalidTokenException invalidTokenException = (InvalidTokenException) authException.getCause();
            responseResult.setMsg("token验证失败，请重新登录获取!");
            responseResult.setExtraData(authException.getCause().getMessage());
        } else {
            responseResult.setCode(ResponseConst.CODE_NO_LOGIN);
            responseResult.setMsg(ResponseConst.CODE_NO_LOGIN_MSG);
        }

        if (isAjax) {
            response.setContentType("application/json");
        } else {
            response.setContentType("text/html;charset=utf-8");
        }
        response.getWriter().println(JsonUtils.toJsonString(responseResult));
    }
}

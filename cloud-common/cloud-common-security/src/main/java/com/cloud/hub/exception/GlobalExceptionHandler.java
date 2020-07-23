package com.cloud.hub.exception;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.consts.ResponseConst;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理，不做引入，各微服务需要引入的使用 @import
 * @author jaxMine
 * @date 2020/4/2
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AuthenticationCredentialsNotFoundException.class)
    @ResponseBody
    public ResponseResult noLoginException(Exception e){
        return new ResponseResult(ResponseConst.CODE_NO_LOGIN, ResponseConst.CODE_NO_LOGIN_MSG);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public ResponseResult accessDeniedException(AccessDeniedException e) {
        return new ResponseResult(ResponseConst.CODE_NO_LOGIN, ResponseConst.MSG_DENY_MESSAGE);
    }
}

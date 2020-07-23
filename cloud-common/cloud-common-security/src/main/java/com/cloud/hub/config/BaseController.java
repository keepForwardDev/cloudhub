package com.cloud.hub.config;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.ShiroUser;
import com.cloud.hub.consts.ResponseConst;
import com.cloud.hub.utils.LoginUserUtil;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BaseController {

    protected final String REDIRECT = "redirect:";
    protected final String FORWARD = "forward:";

    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public HttpServletResponse getResponse(){
        HttpServletResponse  response = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    public HttpSession getSession() {
        return Objects.requireNonNull(getRequest()).getSession();
    }

    public ModelAndView getModelAndView(){
        return new ModelAndView();
    }

    public Long getCurrentUserId(){
        if(getCurrentUser()!=null){
            return getCurrentUser().getId();
        }
        return null;
    }

    /**
     * 当前用户
     * @return
     */
    public ShiroUser getCurrentUser(){
        return LoginUserUtil.getCurrentUser();
    }

    /**
     * 获取请求全路径
     * @param request
     * @return
     */
    public String getFullURL(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append("?");
            url.append(request.getQueryString());
        }
        return url.toString();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    public void successResponse(ResponseResult responseResult) {
        responseResult.setCode(ResponseConst.CODE_SUCCESS);
        responseResult.setMsg(ResponseConst.CODE_SUCCESS_STR);
    }

    public ResponseResult formValidateResult(BindingResult bindingResult) {
        List<ObjectError> error = bindingResult.getAllErrors();
        ResponseResult responseResult = new ResponseResult();
        if (error.size() > 0) {
            responseResult.setMsg(error.get(0).getDefaultMessage());
            return responseResult;
        }
        return null;
    }

}

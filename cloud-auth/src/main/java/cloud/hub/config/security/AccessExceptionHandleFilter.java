package cloud.hub.config.security;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.consts.ResponseConst;
import com.cloud.hub.utils.WebUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class AccessExceptionHandleFilter extends ExceptionTranslationFilter {

    private  ObjectMapper mapper = new ObjectMapper();

    private RequestCache requestCache;


    private String loginUrl = "/login";


    public AccessExceptionHandleFilter(AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationEntryPoint);
        this.requestCache = new HttpSessionRequestCache();
    }

    public AccessExceptionHandleFilter(AuthenticationEntryPoint authenticationEntryPoint, RequestCache requestCache) {
        super(authenticationEntryPoint, requestCache);
        this.requestCache = requestCache;
    }

    @Override
    protected void sendStartAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, AuthenticationException reason) throws ServletException, IOException {
        if (WebUtils.isAjaxRequest(request)) {
            writeJsonToResponse(response, ajaxExceptionMsg(request, reason));
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        requestCache.saveRequest(request, response);
        response.sendRedirect(loginUrl);
    }

    protected ResponseResult ajaxExceptionMsg(HttpServletRequest request,AuthenticationException reason) {
        ResponseResult responseResult = defaultMsg();
        HttpSession session = request.getSession();
        AuthenticationException lastException = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (lastException==null) {
            lastException = reason;
        }
        responseResult.setCode(ResponseConst.CODE_ERROR);
        // 更多异常在  DaoAuthenticationProvider #additionalAuthenticationChecks
        if (lastException instanceof BadCredentialsException ||
                lastException instanceof InternalAuthenticationServiceException) {
            responseResult.setMsg("用户名或者密码错误！");
        } else if (lastException instanceof DisabledException) {
            responseResult.setMsg("该用户已被禁用！");
        } else {
            responseResult.setMsg(ResponseConst.CODE_ERROR_STR);
        }
        return responseResult;
    }

    protected ResponseResult defaultMsg() {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(ResponseConst.CODE_NO_LOGIN);
        responseResult.setMsg(ResponseConst.CODE_NO_LOGIN_MSG);
        return responseResult;
    }

    protected String writeObjectToJsonString(ResponseResult responseResult) {
        String value = StringUtils.EMPTY;
        try {
            value = mapper.writeValueAsString(responseResult);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

    class SupportAjaxAccessDeniedHandlerImpl implements AccessDeniedHandler {

        /**
         * 没权限访问页面
         */
        private String denyAccessUrl;

        public SupportAjaxAccessDeniedHandlerImpl(String denyAccessUrl) {
            this.denyAccessUrl = denyAccessUrl;
        }

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            ResponseResult responseResult = defaultMsg();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (WebUtils.isAjaxRequest(request)) {
                if (authentication==null) {
                    writeJsonToResponse(response, responseResult);
                    return ;
                }
                responseResult.setCode(ResponseConst.CODE_ERROR);
                // 匿名用户访问权限资源
                if (AnonymousAuthenticationToken.class.equals(authentication.getClass())) {

                } else if (authentication.isAuthenticated()) { // 登录用户访问未授权资源
                    responseResult.setMsg("您没有权限访问该资源！");

                }
                writeJsonToResponse(response, responseResult);
            } else {
                response.sendRedirect(denyAccessUrl);
            }
        }
    }

    private void writeJsonToResponse(HttpServletResponse response,ResponseResult result)  {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(writeObjectToJsonString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

}

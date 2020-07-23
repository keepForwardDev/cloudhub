package com.cloud.hub.config.security;

import com.cloud.hub.exception.CustomAccessDeniedHandler;
import com.cloud.hub.exception.CustomAuthenticationEntryPoint;
import com.cloud.hub.utils.TypeUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private BearerTokenExtractor tokenExtractor = new BearerTokenExtractor();

    /**
     * 是否开启session保存用户认证
     */
    @Value("${security.oauth2.resource.enableSessionRepository:true}")
    private Boolean enableSessionRepository;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new OAuth2RequestedMatcher()).authorizeRequests()
                .anyRequest().authenticated();
        // 默认的资源服务器使用的是NullSecurityContextRepository，不对session进行缓存的，考虑到每次认证都需要发送http去认证服务器，获取用户信息
        // 使用session进行缓存, 需要注意的是打开了的话，登出会复杂一点，网关层首先销毁session，然后去认证服务器销毁token，并且清理各下游服务的session，比较复杂，还是不开启的好
        if (enableSessionRepository) {
            http.securityContext().securityContextRepository(new HttpSessionSecurityContextRepository());
        }
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 必须和客户端resourcesId一致
        // 注意这里是资源服务器的id，每个资源服务器应该不同，在资源服务器校验时会校验，构造 OAuth2AuthenticationManager 的resourceId
        //resources.resourceId("user-authentication");
        resources
                // 登录用户鉴权失败
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                // 未登录用户鉴权失败
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                // 定义如何获取token 默认从header 或者 请求参数access_token中获取
                .tokenExtractor(tokenExtractor);
                // 自定义拿到token如何解析token 默认userInfoTokenServices RemoteTokenServices
                //.tokenServices();
                // Authentication 主要在OAuth2AuthenticationProcessingFilter 中设置认证的其他信息，例如可以设置用户信息在此
                //.authenticationDetailsSource();
                // 可以使用注解表达式，配置解析器 SecurityExpressionRoot 里面有很多表达式方法
                //.expressionHandler();
    }

    /**
     * 判断来源请求是否包含oauth2授权信息<br>
     * url参数中含有access_token,或者header里有Authorization
     */
    private class OAuth2RequestedMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            SecurityContext context = (SecurityContext) request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            String existToken = null;
            if (context != null) {
                existToken = ((OAuth2AuthenticationDetails)context.getAuthentication().getDetails()).getTokenValue();
                // 防止用户登录context为空
                SecurityContextHolder.setContext(context);
            }
            Authentication authentication = tokenExtractor.extract(request);
            if (authentication == null) {
                return false;
            }
            String token = TypeUtil.toStr(authentication.getPrincipal());
            // 只有当token不为空 并且当前登录用户的token 不跟 传入的token相等的才能进入oauth2验证流程
            if (StringUtils.isNotBlank(token) && !token.equals(existToken)) {
                return true;
            }
            return false;
        }
    }
}

package cloud.hub.config.security.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * OAuth2AuthenticationProcessingFilter 资源服务器会拦截
 * ResourceServer 到底有什么用，
 *
 *
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(new OAuth2RequestedMatcher()).authorizeRequests()
                .anyRequest().authenticated();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 必须和客户端resourcesId一致
        // 注意这里是资源服务器的id，每个资源服务器应该不同，在资源服务器校验时会校验，构造 OAuth2AuthenticationManager 的resourceId
        resources.resourceId(null);
    }

    /**
     * 判断来源请求是否包含oauth2授权信息<br>
     * url参数中含有access_token,或者header里有Authorization
     */
    private static class OAuth2RequestedMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            // 请求参数中包含access_token参数
            if (request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) != null) {
                return true;
            }

            // 头部的Authorization值以Bearer开头
            String auth = request.getHeader("Authorization");
            if (auth != null) {
                return auth.startsWith(OAuth2AccessToken.BEARER_TYPE);
            }

            return false;
        }
    }
}

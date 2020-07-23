package cloud.hub.config.security;

import cloud.hub.service.UserDetailServiceImpl;
import com.cloud.hub.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.ignoreUrl:''}")
    private String ignoreUrl;

    /**
     * 配置资源拦截访问
     * @param http
     * @throws Exception
     */
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().and().authorizeRequests()
                .antMatchers("/oauth/*").permitAll().anyRequest().authenticated();
        http.csrf().disable().httpBasic().disable();
    }


    public void configure(WebSecurity web) throws Exception {
        if (StringUtils.isNotBlank(ignoreUrl)) {
            // 忽略静态资源
            web.ignoring().antMatchers(ignoreUrl.split(",")).requestMatchers(request -> {
                return SpringUtil.isStaticResource(request);
            });
        }
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService(){
        return new UserDetailServiceImpl();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        //getHttp().getSharedObject(FilterSecurityInterceptor.class).getAuthenticationManager();
        return super.authenticationManagerBean();
    }

}

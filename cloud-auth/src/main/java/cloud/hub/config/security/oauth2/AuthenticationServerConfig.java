package cloud.hub.config.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@EnableAuthorizationServer
@Configuration
public class AuthenticationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisClientDetailsService redisClientDetailsService;

    @Autowired
    private RedisAuthorizationCodeServices redisAuthorizationCodeServices;

    @Autowired
    private RedisJdbcTokenStore redisJdbcTokenStore;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 设置 spring security springSecurityFilterChain链
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("permitAll()");
    }

    /**
     * 设置第三方应用信息
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 从jdbc中读取客户端信息
        clients.withClientDetails(redisClientDetailsService);
    }

    /**
     * 设置授权服务器端点
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //     RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        // 设置统一认证管理器，生成的令牌jdbc存储，并用redis缓存下来，设置授权码存储为redis存储
        endpoints.authenticationManager(authenticationManager).tokenStore(redisJdbcTokenStore).
                authorizationCodeServices(redisAuthorizationCodeServices).userDetailsService(userDetailsService);
    }

}

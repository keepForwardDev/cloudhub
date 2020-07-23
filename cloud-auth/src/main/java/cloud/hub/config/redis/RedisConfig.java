package cloud.hub.config.redis;

import cloud.hub.config.security.oauth2.RedisJdbcTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        return redisMessageListenerContainer;

    }

    @Bean
    public ExpireKeyListener keyExpiredListener(RedisMessageListenerContainer redisMessageListenerContainer, RedisJdbcTokenStore redisJdbcTokenStore) {
        return new ExpireKeyListener(redisMessageListenerContainer, redisJdbcTokenStore);
    }

}

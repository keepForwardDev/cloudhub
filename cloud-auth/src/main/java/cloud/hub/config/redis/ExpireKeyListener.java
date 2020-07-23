package cloud.hub.config.redis;

import cloud.hub.config.security.oauth2.RedisJdbcTokenStore;
import com.cloud.hub.utils.RedisUtil;
import com.cloud.hub.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class ExpireKeyListener extends KeyExpirationEventMessageListener {

    private Logger logger = LoggerFactory.getLogger(ExpireKeyListener.class);

    private RedisJdbcTokenStore redisJdbcTokenStore;

    public ExpireKeyListener(RedisMessageListenerContainer listenerContainer, RedisJdbcTokenStore redisJdbcTokenStore) {
        super(listenerContainer);
        this.redisJdbcTokenStore = redisJdbcTokenStore;
    }

    @Override
    protected void doHandleMessage(Message message) {
        String key = message.toString();
        if (StringUtils.isNotBlank(key)) {
            removeAccessToken(key);
            removeRefreshToken(key);
        }
    }

    private void removeAccessToken(String key) {
        if (key.startsWith(RedisJdbcTokenStore.ACCESS)) {
            String requestId = UUIDUtils.genUUid();
            boolean lockFlag = RedisUtil.tryLock(RedisUtil.LOCK_PREFIX + key, requestId, 10);
            if (lockFlag) {
                String accessToken = key.replace(RedisJdbcTokenStore.ACCESS, "");
                redisJdbcTokenStore.removeAccessToken(accessToken);
                logger.info("refresh token has been expired, remove the data:" + accessToken);
            }
        }
    }

    private void removeRefreshToken(String key) {
        if (key.startsWith(RedisJdbcTokenStore.REFRESH)) {
            String requestId = UUIDUtils.genUUid();
            boolean lockFlag = RedisUtil.tryLock(RedisUtil.LOCK_PREFIX + key, requestId, 10);
            if (lockFlag) {
                String refreshToken = key.replace(RedisJdbcTokenStore.REFRESH, "");
                redisJdbcTokenStore.removeRefreshToken(refreshToken);
                logger.info("access token has been expired, remove the data:" + refreshToken);
            }
        }
    }

}

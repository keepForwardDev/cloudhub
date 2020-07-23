package cloud.hub.controller;

import cloud.hub.config.security.oauth2.RedisJdbcTokenStore;
import cloud.hub.service.UserService;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.config.BaseController;
import com.cloud.hub.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("oauth")
public class Oauth2Controller extends BaseController {

    @Autowired
    private RedisJdbcTokenStore redisJdbcTokenStore;

    @Autowired
    private UserService userService;

    @GetMapping("/currentUserInfo")
    public Authentication principal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

    @GetMapping("/getUserByPhone")
    public User getUserByPhone(String phone) {
       return userService.findByPhone(phone);
    }

    /**
     * 用户登出的时候可以调用该端点
     * @param token
     * @return
     */
    @PostMapping("deleteToken/{token}")
    public ResponseResult deleteToken(@PathVariable(value = "token") String token) {
        redisJdbcTokenStore.removeAccessToken(token);
        return ResponseResult.getSuccessInstance();
    }
}

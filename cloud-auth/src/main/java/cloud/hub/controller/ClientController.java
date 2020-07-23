package cloud.hub.controller;

import cloud.hub.config.security.MyBCryptPasswordEncoder;
import cloud.hub.config.security.oauth2.RedisClientDetailsService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.config.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理第三方应用
 */
@RestController
@RequestMapping("client")
public class ClientController extends BaseController {

    @Autowired
    private MyBCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisClientDetailsService clientDetailsService;

    @PreAuthorize("hasRole('client-manager')")
    @PostMapping
    public void save(@RequestBody BaseClientDetails clientDetails) {
        ClientDetails client = getAndCheckClient(clientDetails.getClientId(), false);
        if (client != null) {
            throw new IllegalArgumentException(clientDetails.getClientId() + "已存在");
        }
        // 密码加密
        clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));

        clientDetailsService.addClientDetails(clientDetails);
    }

    @PreAuthorize("hasRole('client-manager')")
    @PutMapping
    public void update(@RequestBody BaseClientDetails clientDetails) {
        getAndCheckClient(clientDetails.getClientId(), true);
        clientDetailsService.updateClientDetails(clientDetails);
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping(value = "/{clientId}", params = "secret")
    public void updateSecret(@PathVariable String clientId, String secret) {
        getAndCheckClient(clientId, true);
        secret = passwordEncoder.encode(secret);
        clientDetailsService.updateClientSecret(clientId, secret);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResponseResult findClients(Page page) {
        List<ClientDetails> clientDetails = clientDetailsService.listClientDetails();
        return null;
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/{clientId}")
    public ClientDetails getById(@PathVariable String clientId) {
        return getAndCheckClient(clientId, true);
    }

    @PreAuthorize("hasAuthority('client:del')")
    @DeleteMapping("/{clientId}")
    public void delete(@PathVariable String clientId) {
        getAndCheckClient(clientId, true);
        clientDetailsService.removeClientDetails(clientId);
    }

    /**
     * 根据id获取client信息
     *
     * @param clientId
     * @param check    是否校验存在性
     * @return
     */
    private ClientDetails getAndCheckClient(String clientId, boolean check) {
        ClientDetails clientDetails = null;
        try {
            clientDetails = clientDetailsService.loadClientByClientId(clientId);
        } catch (NoSuchClientException e) {
            if (check) {
                throw new IllegalArgumentException(clientId + "不存在");
            }
        }

        return clientDetails;
    }

    @RequestMapping("hello")
    public String hello() {
        return "hello";
    }

}

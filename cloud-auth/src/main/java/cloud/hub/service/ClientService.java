package cloud.hub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ClientService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String CLIENT_TABLE_NAME = "oauth_client_details";

    private Page pageList(Page page) {
        return null;
    }
}

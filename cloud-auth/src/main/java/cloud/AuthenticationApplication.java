package cloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: jaxMine
 * @Date: 2019/12/31 17:34
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.cloud.hub.mapper.**"})
@EnableDiscoveryClient
public class AuthenticationApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class,args);
    }

}

package com.cloud;

import com.cloud.hub.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.cloud.hub.mapper.**"})
@EnableDiscoveryClient
@Import(GlobalExceptionHandler.class)
public class SystemApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
}

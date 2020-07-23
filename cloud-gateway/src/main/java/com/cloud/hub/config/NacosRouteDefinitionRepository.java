package com.cloud.hub.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 配置动态路由
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.cloud.gateway", name="enableDynamicRoute", havingValue = "true")
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository, InitializingBean {

    /**
     * nacos配置dataId
     */
    @Value("${nacos.gateway.route.dataId:gateway-route}")
    private String nacosConfigDataId;

    /**
     * nacos配置 组id
     */
    @Value("${nacos.gateway.route.groupId:DEFAULT_GROUP}")
    private String nacosConfigGroupId;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            String jsonContent = nacosConfigProperties.configServiceInstance().getConfig(nacosConfigDataId, nacosConfigGroupId, 5000l);
            List<RouteDefinition> routeDefinitions = parseContent(jsonContent);
            return Flux.fromIterable(routeDefinitions);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return Flux.fromIterable(new ArrayList<>());
    }

    private List<RouteDefinition> parseContent(String content) {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        routeDefinitions =  JSONObject.parseArray(content, RouteDefinition.class);
        return routeDefinitions;
    }


    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        addListener();
    }

    private void addListener() {
        try {
            nacosConfigProperties.configServiceInstance().addListener(nacosConfigDataId, nacosConfigGroupId, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}

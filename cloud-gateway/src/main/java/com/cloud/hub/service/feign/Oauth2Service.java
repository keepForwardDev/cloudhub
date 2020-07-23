package com.cloud.hub.service.feign;

import com.cloud.hub.bean.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "user-authentication")
public interface Oauth2Service {

    @RequestMapping(value = "/auth/oauth/deleteToken/{token}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,method = RequestMethod.POST)
    ResponseResult deleteToken(@PathVariable(value = "token") String token);
}

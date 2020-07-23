package com.cloud.hub.controller;

import com.cloud.hub.bean.ResponseResult;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @RequestMapping("/defaultFallback")
    @ResponseBody
    public ResponseResult sayHi(Authentication authentication) {
        return new ResponseResult(-1, "服务器开小差了~");
    }
}

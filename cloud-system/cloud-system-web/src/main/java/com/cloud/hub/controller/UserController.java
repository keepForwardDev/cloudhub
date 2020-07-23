package com.cloud.hub.controller;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.UserBean;
import com.cloud.hub.common.CommonPage;
import com.cloud.hub.config.BaseController;
import com.cloud.hub.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserManageService userManageService;

    @RequestMapping("save")
    public ResponseResult save(@RequestBody @Valid UserBean bean, BindingResult bindingResult) {
        ResponseResult responseResult = formValidateResult(bindingResult);
        return responseResult == null ? userManageService.save(bean) : responseResult;
    }

    @RequestMapping("delete")
    public ResponseResult delete(@RequestBody List<Long> idList) {
        userManageService.logicDelete(idList);
        return ResponseResult.getSuccessInstance();
    }

    @RequestMapping("list")
    public ResponseResult pageList(CommonPage page, UserBean bean) {
        return userManageService.pageList(page, userManageService.defaultOrderQueryCondition(bean));
    }

    @RequestMapping("resetPassword")
    public ResponseResult resetPassword(@RequestBody List<Long> idList) {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(userManageService.resetPassword(idList));
        return responseResult;
    }
}

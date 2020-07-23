package com.cloud.hub.controller;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.RoleBean;
import com.cloud.hub.common.CommonPage;
import com.cloud.hub.config.BaseController;
import com.cloud.hub.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/save")
    public ResponseResult save(@RequestBody @Valid RoleBean roleBean, BindingResult bindingResult) {
        ResponseResult responseResult = formValidateResult(bindingResult);
        return responseResult == null ? roleService.saveRole(roleBean) : responseResult;
    }

    @RequestMapping("delete/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseResult.getSuccessInstance();
    }

    @RequestMapping("/list")
    public ResponseResult list(CommonPage page, RoleBean bean) {
        return roleService.pageList(page, roleService.defaultOrderQueryCondition(bean));
    }

    @RequestMapping("/labelNodes")
    public ResponseResult labelNodes() {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(roleService.getLabelNodes());
        return responseResult;
    }

}

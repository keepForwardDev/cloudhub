package com.cloud.hub.controller;

import com.cloud.hub.bean.DepartmentBean;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.common.CommonPage;
import com.cloud.hub.config.BaseController;
import com.cloud.hub.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("department")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @RequestMapping("/save")
    public ResponseResult save(@RequestBody @Valid DepartmentBean bean, BindingResult bindingResult) {
        ResponseResult responseResult = formValidateResult(bindingResult);
        departmentService.saveEntity(bean);
        return responseResult == null ? ResponseResult.getSuccessInstance() : responseResult;
    }

    @RequestMapping("delete")
    public ResponseResult delete(@RequestBody List<Long> idList) {
        departmentService.logicDelete(idList);
        return ResponseResult.getSuccessInstance();
    }

    @RequestMapping("/list")
    @PreAuthorize("hasRole('admin')")
    public ResponseResult list(CommonPage page, DepartmentBean bean) {
        return departmentService.pageList(page, departmentService.defaultOrderQueryCondition(bean));
    }

    @RequestMapping("tree")
    @PreAuthorize("hasRole('dds')")
    public ResponseResult treeList() {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(departmentService.getDepartmentTree());
        return responseResult;
    }

    @RequestMapping("info/{id}")
    public ResponseResult info(@PathVariable Long id) {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(departmentService.convert(departmentService.getById(id)));
        return responseResult;
    }
}

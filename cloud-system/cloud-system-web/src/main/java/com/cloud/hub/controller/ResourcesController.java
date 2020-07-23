package com.cloud.hub.controller;


import com.cloud.hub.bean.ResourceBean;
import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.common.CommonPage;
import com.cloud.hub.config.BaseController;
import com.cloud.hub.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("resource")
@RestController
public class ResourcesController extends BaseController {

    @Autowired
    private ResourceService resourceService;

    @RequestMapping("save")
    public ResponseResult save(@RequestBody @Valid ResourceBean bean, BindingResult bindingResult) {
        ResponseResult responseResult = formValidateResult(bindingResult);
        return responseResult == null ? resourceService.save(bean) : responseResult;
    }

    @RequestMapping("delete/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        resourceService.removeById(id);
        return ResponseResult.getSuccessInstance();
    }

    @RequestMapping("list")
    public ResponseResult listPage(CommonPage page, ResourceBean bean) {
        return resourceService.pageList(page, resourceService.defaultOrderQueryCondition(bean));
    }

    /**
     * lazy load
     * @param menuId
     * @return
     */
    @RequestMapping("menuResource/{menuId}")
    public ResponseResult menuResource(@PathVariable Long menuId) {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(resourceService.menuResources(menuId));
        return responseResult;
    }

    /**
     * all load
     * @return
     */
    @RequestMapping("menuResource")
    public ResponseResult menuResource() {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(resourceService.menuResources());
        return responseResult;
    }

}

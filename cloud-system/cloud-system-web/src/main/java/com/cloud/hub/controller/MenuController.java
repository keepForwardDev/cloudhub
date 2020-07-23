package com.cloud.hub.controller;

import com.cloud.hub.bean.ResponseResult;
import com.cloud.hub.bean.Router;
import com.cloud.hub.common.CommonPage;
import com.cloud.hub.config.BaseController;
import com.cloud.hub.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("menu")
@RestController
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("list")
    public ResponseResult menuList(CommonPage page, String title) {
        return menuService.getRootMenuList(page, title);
    }

    @RequestMapping("childMenu/{parentId}")
    public ResponseResult childMenu(@PathVariable(value = "parentId") Long parentId) {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(menuService.getMenuListByParentId(parentId));
        return responseResult;
    }

    @RequestMapping("save")
    public ResponseResult saveMenu(@RequestBody @Valid Router router, BindingResult result) {
        ResponseResult responseResult = formValidateResult(result);
        return responseResult == null ? menuService.saveMenu(router) : responseResult;
    }

    @RequestMapping("treeList")
    public ResponseResult menuTree(Long parentId) {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(menuService.getMenuTree(parentId));
        return responseResult;
    }

    @RequestMapping("delete/{id}")
    public ResponseResult delete(@PathVariable(value = "id") Long id) {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        menuService.deleteMenu(id);
        return responseResult;
    }

    @RequestMapping("info/{id}")
    public ResponseResult menuInfo(@PathVariable Long id) {
        ResponseResult responseResult = ResponseResult.getSuccessInstance();
        responseResult.setData(menuService.getRouterById(id));
        return responseResult;
    }

}

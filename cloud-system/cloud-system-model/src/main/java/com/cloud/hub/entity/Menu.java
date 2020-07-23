package com.cloud.hub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.hub.common.CommonModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 菜单
 */
@Entity
@Table(name="sys_menu")
@TableName("sys_menu")
public class Menu extends CommonModel<Menu> {
    /**
     * 页面的export_name
     */
    private String name;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 是否隐藏
     */
    private Integer hidden = 0;

    /**
     * 是否一直显示主菜单
     */
    private Integer alwaysShow;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 路由meta
     */
    private String meta;

    private Integer sort = 0;

    private Long parentId = 0l;

    private Integer hasChildren = 0;

    /**
     * 路由访问地址
     */
    private String path;

    /**
     * 组件地址
     */
    private String component;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getHidden() {
        return hidden;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Integer getAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(Integer alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    @Column(columnDefinition="text")
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Integer hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}

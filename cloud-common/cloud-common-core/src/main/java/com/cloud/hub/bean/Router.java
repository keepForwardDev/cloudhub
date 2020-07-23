package com.cloud.hub.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 路由
 */
public class Router implements Serializable{

    private Long id;

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
    private boolean hidden = false;

    /**
     * 是否一直显示主菜单
     */
    private boolean alwaysShow = false;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 路由meta
     */
    private Meta meta = new Meta();

    private Integer sort = 0;

    private Long parentId = 0l;

    private boolean hasChildren = false;

    /**
     * 路由访问地址
     */
    private String path;

    /**
     * 组件地址
     */
    private String component;


    private List<Router> children = new ArrayList<>();

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


    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
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

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getPath() {
        return path;
    }

    public void setChildren(List<Router> children) {
        this.children = children;
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

    public List<Router> getChildren() {
        return children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static class Meta implements Serializable{

        private String title;

        private String icon;

        private Boolean  noCache = false;

        private Boolean affix = false;

        private Boolean breadcrumb = true;

        private String activeMenu;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public Boolean getNoCache() {
            return noCache;
        }

        public void setNoCache(Boolean noCache) {
            this.noCache = noCache;
        }

        public Boolean getAffix() {
            return affix;
        }

        public void setAffix(Boolean affix) {
            this.affix = affix;
        }

        public Boolean getBreadcrumb() {
            return breadcrumb;
        }

        public void setBreadcrumb(Boolean breadcrumb) {
            this.breadcrumb = breadcrumb;
        }

        public String getActiveMenu() {
            return activeMenu;
        }

        public void setActiveMenu(String activeMenu) {
            this.activeMenu = activeMenu;
        }
    }
}

package com.cloud.hub.bean;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ResourceBean extends BaseEntityBean {

    /**
     * 资源编码， 唯一
     */
    @NotBlank(message = "资源编码不能为空！")
    private String code;

    /**
     * 资源链接
     */
    private String url;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空！")
    private String name;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 资源展示排序
     */
    private Integer sort=0;

    /**
     * 所属菜单
     */
    @NotNull(message = "所属菜单不能为空！")
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


}

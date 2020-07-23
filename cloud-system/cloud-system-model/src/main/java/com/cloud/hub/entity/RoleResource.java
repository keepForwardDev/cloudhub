package com.cloud.hub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.hub.common.CommonModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色与资源的关系，包含菜单
 */
@Entity
@Table(name="sys_role_resource")
@TableName("sys_role_resource")
public class RoleResource extends CommonModel<RoleResource> {

    private Long roleId;

    private Long resourceId;

    private Long menuId;

    /**
     * 是否菜单，0否，1是
     */
    private Integer menuFlag=0;

    private String cascaderValue;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Integer getMenuFlag() {
        return menuFlag;
    }

    public void setMenuFlag(Integer menuFlag) {
        this.menuFlag = menuFlag;
    }

    @Column(columnDefinition="text")
    public String getCascaderValue() {
        return cascaderValue;
    }

    public void setCascaderValue(String cascaderValue) {
        this.cascaderValue = cascaderValue;
    }
}

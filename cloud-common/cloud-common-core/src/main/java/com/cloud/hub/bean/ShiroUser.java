package com.cloud.hub.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShiroUser implements Serializable {
    private Long id;

    private Long deptId;

    private String departmentName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 账号
     */
    private String account;

    /**
     * 名字
     */
    private String name;

    /**
     * 电子邮件
     */
    private String email;

    private String uuid;

    /**
     * 角色code
     */
    private List<String> rolesCode= new ArrayList<>();

    /**
     * 角色资源code
     */
    private List<String> resourcesCode= new ArrayList<>();

    /**
     * 菜单列表
     */
    private List<Router> menu= new ArrayList<>();

    private String roleName;

    private Integer enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRolesCode() {
        return rolesCode;
    }

    public void setRolesCode(List<String> rolesCode) {
        this.rolesCode = rolesCode;
    }

    public List<String> getResourcesCode() {
        return resourcesCode;
    }

    public void setResourcesCode(List<String> resourcesCode) {
        this.resourcesCode = resourcesCode;
    }

    public List<Router> getMenu() {
        return menu;
    }

    public void setMenu(List<Router> menu) {
        this.menu = menu;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

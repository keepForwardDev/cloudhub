package com.cloud.hub.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserBean extends BaseEntityBean {

    private String account;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 真实名字
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    private Integer sex = 1;

    /**
     * 电话
     */
    private String phone;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 所属部门
     */
    private Long deptId;

    /**
     * 所属部门名称
     */
    private String deptName;

    /**
     * 是否启用 0 否 1 是
     */
    private Integer enabled = 0;

    /**
     * 职务
     */
    private String duty;

    /**
     * 角色
     */
    private List<LabelNode> role = new ArrayList<>();

    /**
     * 单个角色id
     */
    private Long roleId;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public List<LabelNode> getRole() {
        return role;
    }

    public void setRole(List<LabelNode> role) {
        this.role = role;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}

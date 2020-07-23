package com.cloud.hub.bean;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class DepartmentBean {
    private Long id;
    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空！")
    private String name;

    /**
     * 父级id
     */
    private Long parentId;

    private String parentName;

    /**
     * 全名
     */
    private String fullName;

    /**
     * 部门编码
     */
    private String levelCode;

    /**
     * 主负责人
     */
    private Long mainChargeUserId;

    private String mainChargeUserName;

    /**
     * 排序号
     */
    private Integer sort;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Long getMainChargeUserId() {
        return mainChargeUserId;
    }

    public void setMainChargeUserId(Long mainChargeUserId) {
        this.mainChargeUserId = mainChargeUserId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getMainChargeUserName() {
        return mainChargeUserName;
    }

    public void setMainChargeUserName(String mainChargeUserName) {
        this.mainChargeUserName = mainChargeUserName;
    }
}

package com.cloud.hub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.hub.common.CommonModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="sys_department", uniqueConstraints={@UniqueConstraint(columnNames={"levelCode"})})
@TableName("sys_department")
public class Department extends CommonModel<Department> {
    /**
     * 公司名称
     */
    private String name;

    /**
     * 父级id
     */
    private Long parentId;

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

    /**
     * 副负责人
     */
    private Long assistantChargeUserId;

    /**
     * 排序号
     */
    private Integer sort;

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

    public Long getAssistantChargeUserId() {
        return assistantChargeUserId;
    }

    public void setAssistantChargeUserId(Long assistantChargeUserId) {
        this.assistantChargeUserId = assistantChargeUserId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}

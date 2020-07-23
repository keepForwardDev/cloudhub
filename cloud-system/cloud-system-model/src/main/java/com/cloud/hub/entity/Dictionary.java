package com.cloud.hub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.hub.common.CommonModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @Author: jaxMine
 * @Date: 2020/1/3 16:45
 */
@Entity
@Table(name ="sys_dictionary",uniqueConstraints={@UniqueConstraint(columnNames={"code"})})
@TableName("sys_dictionary")
public class Dictionary extends CommonModel<Dictionary> {

    private String name;

    /**
     * 唯一标识
     */
    private String code;


    /**
     * 父级id
     */
    private Long parentId = 0l;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 描述
     */
    private String description;

    private Boolean hasChildren = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}

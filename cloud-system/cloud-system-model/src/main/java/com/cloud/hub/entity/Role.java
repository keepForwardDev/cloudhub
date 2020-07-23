package com.cloud.hub.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.hub.common.CommonModel;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 角色表
 */
@Entity
@Table(name = "sys_role", uniqueConstraints={@UniqueConstraint(columnNames={"code"})})
@TableName("sys_role")
public class Role extends CommonModel<Role> {
    /**
     * 角色名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 角色编码，唯一
     */
    private String code;


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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

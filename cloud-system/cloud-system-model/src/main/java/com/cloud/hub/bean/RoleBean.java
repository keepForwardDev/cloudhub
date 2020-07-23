package com.cloud.hub.bean;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoleBean extends BaseEntityBean implements Serializable {

    @NotBlank(message = "必须填写角色名称！")
    private String name;

    @NotBlank(message = "必须填写角色编码！")
    private String code;

    private String description;

    private List<Long> menuIds = new ArrayList<>();

    /**
     * 资源id 负数id
     */
    private List<Long> resourceIds = new ArrayList<>();

    private List<List<Long>> cascaderValue = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public List<Long> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }


    public List<List<Long>> getCascaderValue() {
        return cascaderValue;
    }

    public void setCascaderValue(List<List<Long>> cascaderValue) {
        this.cascaderValue = cascaderValue;
    }
}

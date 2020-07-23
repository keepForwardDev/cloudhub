package com.cloud.hub.bean;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class DictionaryBean extends TreeBaseBean<DictionaryBean> {

    @NotBlank(message = "字典名称必须填写！")
    private String name;

    /**
     * 唯一标识
     */
    private String code;

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

    private Date createTime;

    /**
     * 排序字段
     */
    private String orderField;

    /**
     * 排序方式
     */
    private String orderWay = "descending";

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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay;
    }
}

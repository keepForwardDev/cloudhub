package com.cloud.hub.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class BaseEntityBean {

    private Long id;

    private Date createTime;

    /**
     * 排序字段
     */
    private String orderField;

    /**
     * 排序方式
     */
    private String orderWay = "descending";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonIgnore
    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    @JsonIgnore
    public String getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(String orderWay) {
        this.orderWay = orderWay;
    }
}

package com.cloud.hub.bean;

/**
 * @Author: jaxMine
 * @Date: 2019/12/31 16:24
 */
public class ResponseResult {

    /**
     * 状态码
     */
    private int code;


    /**
     * 状态描述信息
     */
    private String msg = "";

    /**
     * 响应数据
     */
    private Object data = "";

    /**
     * 额外的数据
     */
    private Object extraData = "";


    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getExtraData() {
        return extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }
}

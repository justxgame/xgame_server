package com.xgame.service.manager.rest.model.response;

import com.alibaba.fastjson.annotation.JSONField;

public class ServerRes {
    @JSONField(name = "Code")
    private Integer code;

    @JSONField(name = "Desc")
    private String desc;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

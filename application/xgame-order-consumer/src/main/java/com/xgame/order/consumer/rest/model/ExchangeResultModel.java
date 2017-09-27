package com.xgame.order.consumer.rest.model;

import com.alibaba.fastjson.annotation.JSONField;

public class ExchangeResultModel {
    @JSONField(name = "Server_id")
    private Integer serverId;
    @JSONField(name = "Uid")
    private Integer uid;
    @JSONField(name = "Id")
    private Integer id;
    @JSONField(name = "Password")
    private String password;

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "ExchangeResultModel{" +
                "serverId=" + serverId +
                ", uid=" + uid +
                ", id=" + id +
                ", password='" + password + '\'' +
                '}';
    }
}

package com.xgame.service.game.receive.rest.model.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAddressReportModel {
    @JsonProperty("Uid")
    private Integer uid;
    @JsonProperty("Server_id")
    private Integer server_id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Address")
    private String address;
    @JsonProperty("Phone_number")
    private String phone_number;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getServer_id() {
        return server_id;
    }

    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString(){
        return "UserAddressReportModel[uid=" + uid + " server_id=" + server_id + " name="
                + name + " address=" + address + " phone_number" + phone_number + "]";
    }
}

package com.xgame.service.load.balance.db.dto;

public class UserBasicDto {
    private Integer uid;
    private Integer server_id;

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
}

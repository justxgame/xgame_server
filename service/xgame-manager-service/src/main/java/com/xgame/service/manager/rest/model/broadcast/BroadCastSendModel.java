package com.xgame.service.manager.rest.model.broadcast;

public class BroadCastSendModel {
    private Integer server_id;
    private String msg;

    public Integer getServer_id() {
        return server_id;
    }

    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

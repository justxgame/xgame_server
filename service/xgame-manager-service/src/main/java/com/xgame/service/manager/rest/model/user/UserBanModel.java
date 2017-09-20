package com.xgame.service.manager.rest.model.user;

public class UserBanModel {
    private Integer server_id;
    private Integer uid;
    private Integer op_code;

    public Integer getServer_id() {
        return server_id;
    }

    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getOp_code() {
        return op_code;
    }

    public void setOp_code(Integer op_code) {
        this.op_code = op_code;
    }
}

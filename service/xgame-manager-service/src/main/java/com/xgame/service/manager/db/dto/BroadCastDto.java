package com.xgame.service.manager.db.dto;

public class BroadCastDto {
    private Long transection;
    private String server_id;
    private String msg;
    private String send_user;
    private String indate;

    public BroadCastDto(String serverId,String msg,String uid,String indate){
        this.server_id=serverId;
        this.send_user=uid;
        this.msg=msg;
        this.indate=indate;
    }
    public BroadCastDto(){

    }

    public Long getTransection() {
        return transection;
    }

    public void setTransection(Long transection) {
        this.transection = transection;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSend_user() {
        return send_user;
    }

    public void setSend_user(String send_user) {
        this.send_user = send_user;
    }

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }
}

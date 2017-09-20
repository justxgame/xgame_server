package com.xgame.service.game.receive.db.dto;

public class UserStatusDto{

    private Integer uid;

    private Integer server_id;

    private String nick_name;

    private Integer online_flag;

    private String indate;

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

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public Integer getOnline_flag() {
        return online_flag;
    }

    public void setOnline_flag(Integer online_flag) {
        this.online_flag = online_flag;
    }

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }
}

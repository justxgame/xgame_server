package com.xgame.service.game.receive.rest.model.user;

import com.alibaba.fastjson.annotation.JSONField;

public class UserStatusReportModel {
    @JSONField(name = "Uid")
    private Integer uid;
    @JSONField(name = "Server_id")
    private Integer server_id;
    @JSONField(name = "Name")
    private String nick_name;
    @JSONField(name = "Online_flag")
    private Boolean online_flag;

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

    public Boolean getOnline_flag() {
        return online_flag;
    }

    public void setOnline_flag(Boolean online_flag) {
        this.online_flag = online_flag;
    }
    @Override
    public String toString(){
        return "UserStatusReportModel[uid=" + uid + " server_id=" + server_id + " nick_name="
                + nick_name + " online_flag=" + online_flag + "]";
    }
}

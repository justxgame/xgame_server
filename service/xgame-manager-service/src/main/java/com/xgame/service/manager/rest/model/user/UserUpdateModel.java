package com.xgame.service.manager.rest.model.user;

import com.alibaba.fastjson.annotation.JSONField;

public class UserUpdateModel {
    private Integer server_id;
    private Integer pid;
    private Integer diamond;
    private Integer counpon;

    private Integer coins;
    private Integer ticket;

    @JSONField(name = "Server_id")
    public Integer getServer_id() {
        return server_id;
    }


    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    @JSONField(name = "Pid")
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
    @JSONField(name = "Coins")
    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }
    @JSONField(name = "Ticket")
    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }
    @JSONField(name = "Diamond")
    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }
    @JSONField(name = "Counpon")
    public Integer getCounpon() {
        return counpon;
    }

    public void setCounpon(Integer counpon) {
        this.counpon = counpon;
    }
}

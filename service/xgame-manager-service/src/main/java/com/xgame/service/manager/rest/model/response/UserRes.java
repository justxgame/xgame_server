package com.xgame.service.manager.rest.model.response;

import com.alibaba.fastjson.annotation.JSONField;

public class UserRes {

    private Integer serverId;

    private Boolean isExist;

    private Integer pid;

    private String userName;

    private Integer coins;

    private Integer diamond;

    private Integer ticket;

    private Integer counpon;


    public Integer getServerId() {
        return serverId;
    }
    @JSONField(name = "Server_id")
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Boolean getExist() {
        return isExist;
    }
    @JSONField(name = "Is_exist")
    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public Integer getPid() {
        return pid;
    }
    @JSONField(name = "Pid")
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getUserName() {
        return userName;
    }
    @JSONField(name = "Name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCoins() {
        return coins;
    }
    @JSONField(name = "Coins")
    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getDiamond() {
        return diamond;
    }
    @JSONField(name = "Diamond")
    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }

    public Integer getTicket() {
        return ticket;
    }
    @JSONField(name = "Ticket")
    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Integer getCounpon() {
        return counpon;
    }
    @JSONField(name = "Coupon")
    public void setCounpon(Integer counpon) {
        this.counpon = counpon;
    }
}

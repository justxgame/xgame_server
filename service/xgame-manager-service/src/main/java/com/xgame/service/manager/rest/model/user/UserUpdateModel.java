package com.xgame.service.manager.rest.model.user;

public class UserUpdateModel {
    private Integer server_id;
    private Integer uid;
    private Integer diamond;
    private Integer counpon;

    private Integer coins;
    private Integer ticket;


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


    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }

    public Integer getCounpon() {
        return counpon;
    }

    public void setCounpon(Integer counpon) {
        this.counpon = counpon;
    }
}

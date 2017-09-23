package com.xgame.service.manager.rest.model.user;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoModel {
    private Integer actionId;
    private String serverId;

    private String pid;
    private String userName;
    private Integer ticket;
    private Integer diamond;
    @JsonProperty("counpon")
    private Integer coupon;
    private Integer coins;
    private Integer status;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDiamond() {
        return diamond;
    }

    public void setDiamond(Integer diamond) {
        this.diamond = diamond;
    }

    public Integer getCoupon() {
        return coupon;
    }

    public void setCoupon(Integer coupon) {
        this.coupon = coupon;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }


    @Override
    public String toString(){
        return "[ actionid="+actionId+" serverId="+serverId+" pid="+pid+" userName="+userName
                +" ticket="+ticket+" diamond="+diamond+" coupon="+coupon+" coins="+coins
                +" status="+status;
    }

}

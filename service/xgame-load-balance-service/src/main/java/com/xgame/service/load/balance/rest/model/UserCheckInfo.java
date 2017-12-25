package com.xgame.service.load.balance.rest.model;

public class UserCheckInfo {
    private String uname;
    private Integer type;
    private String inviter_uname;
    private String msg;
    private long time;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInviter_uname() {
        return inviter_uname;
    }

    public void setInviter_uname(String inviter_uname) {
        this.inviter_uname = inviter_uname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

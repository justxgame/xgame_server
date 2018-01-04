package com.xgame.service.load.balance.rest.model;

public class UserInfo {
    private String userName;
    private String ver;
    private String platform;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString(){
        return "useName="+userName+" version="+ver+" platform="+platform;
    }
}

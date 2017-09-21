package com.xgame.service.manager.rest.model.broadcast;

public class   BroadCastModel {
    private Long transection;
    private String serverId;
    private String message;
    private String sendUserName;
    private Long sendDate;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public Long getSendDate() {
        return sendDate;
    }

    public void setSendDate(Long sendDate) {
        this.sendDate = sendDate;
    }

    public Long getTransection() {
        return transection;
    }

    public void setTransection(Long transection) {
        this.transection = transection;
    }
}

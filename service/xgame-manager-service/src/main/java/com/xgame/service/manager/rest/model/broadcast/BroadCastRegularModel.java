package com.xgame.service.manager.rest.model.broadcast;

public class BroadCastRegularModel {
    private Long transection;
    private String uid;
    private String serverId;
    private String indate;
    private String nextSendDate;
    private String msg;
    private String freqUnit;
    private Integer freqVal;
    private String startDate;
    private String endDate;

    public Long getTransection() {
        return transection;
    }

    public void setTransection(Long transection) {
        this.transection = transection;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }

    public String getNextSendDate() {
        return nextSendDate;
    }

    public void setNextSendDate(String nextSendDate) {
        this.nextSendDate = nextSendDate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFreqUnit() {
        return freqUnit;
    }

    public void setFreqUnit(String freqUnit) {
        this.freqUnit = freqUnit;
    }

    public Integer getFreqVal() {
        return freqVal;
    }

    public void setFreqVal(Integer freqVal) {
        this.freqVal = freqVal;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

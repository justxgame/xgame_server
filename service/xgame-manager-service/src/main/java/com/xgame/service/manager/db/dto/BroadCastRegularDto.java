package com.xgame.service.manager.db.dto;



public class BroadCastRegularDto {
    private Long transection;
    private String uid;
    private String server_id;
    private String indate;
    private String next_send_date;
    private String msg;
    private String freq_unit;
    private Integer freq_val;
    private String start_date;
    private String end_date;
    private Integer type;

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

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }


    public String getNext_send_date() {
        return next_send_date;
    }

    public void setNext_send_date(String next_send_date) {
        this.next_send_date = next_send_date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFreq_unit() {
        return freq_unit;
    }

    public void setFreq_unit(String freq_unit) {
        this.freq_unit = freq_unit;
    }

    public Integer getFreq_val() {
        return freq_val;
    }

    public void setFreq_val(Integer freq_val) {
        this.freq_val = freq_val;
    }

    public String getIndate() {
        return indate;
    }

    public void setIndate(String indate) {
        this.indate = indate;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return "RewardOrderLogMappingDto{" +
                "server_id='" + server_id + '\'' +
                ", uid='" + uid + '\'' +
                ", transection=" + transection +
                ", indate=" + indate +
                ", next_send_date=" + next_send_date +
                ", msg=" + msg +
                ", freq_unit=" + freq_unit +
                ", freq_val='" + freq_val + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date +
                ", type ="+ type+
                '}';
    }
}

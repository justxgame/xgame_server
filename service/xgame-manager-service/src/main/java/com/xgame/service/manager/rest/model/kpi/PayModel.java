package com.xgame.service.manager.rest.model.kpi;

/**
 * Created by william on 2017/9/30.
 */
public class PayModel {

    private String date;  //日期
    private String server_id; //服务器id
    private String pay;   //支付金额


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
}

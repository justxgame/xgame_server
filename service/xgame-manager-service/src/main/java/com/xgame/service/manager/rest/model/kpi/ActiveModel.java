package com.xgame.service.manager.rest.model.kpi;

/**
 * Created by william on 2017/9/30.
 */
public class ActiveModel {

    private String date;  //日期
    private String server_id;
    private String dau;   //日活人数

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDau() {
        return dau;
    }

    public void setDau(String dau) {
        this.dau = dau;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
}

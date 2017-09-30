package com.xgame.service.manager.rest.model.kpi;

/**
 * Created by william on 2017/9/30.
 */
public class NewActiveModel {

    private String date;  //日期
    private String server_id;
    private String active_num;   //新激活人数


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActive_num() {
        return active_num;
    }

    public void setActive_num(String active_num) {
        this.active_num = active_num;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }
}

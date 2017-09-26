package com.xgame.order.consumer.db.dto;

import java.util.Date;

/**
 * Created by william on 2017/9/27.
 */
public class SubOrderInfoDto {


    private String sub_order_id;
    private String order_id;
    private Integer batch_number;
    private Integer state;
    private String message;
    private Date indate;

    public String getSub_order_id() {
        return sub_order_id;
    }

    public void setSub_order_id(String sub_order_id) {
        this.sub_order_id = sub_order_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getBatch_number() {
        return batch_number;
    }

    public void setBatch_number(Integer batch_number) {
        this.batch_number = batch_number;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }
}

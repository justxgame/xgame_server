package com.xgame.order.consumer.db.dto;

import java.util.Date;

public class RewardOrderInfoDto {

    private String order_id;
    private String server_id;
    private String uid;
    private Integer item_id;
    private Integer item_type;
    private Integer item_count;
    private Integer order_status;
    private String order_exception;
    private Integer is_reorder;
    private Date indate;
    private Integer id;
    private String phone;

    private String message;
    private String callback_message;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getItem_type() {
        return item_type;
    }

    public void setItem_type(Integer item_type) {
        this.item_type = item_type;
    }

    public Integer getItem_count() {
        return item_count;
    }

    public void setItem_count(Integer item_count) {
        this.item_count = item_count;
    }

    public Integer getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Integer order_status) {
        this.order_status = order_status;
    }

    public String getOrder_exception() {
        return order_exception;
    }

    public void setOrder_exception(String order_exception) {
        this.order_exception = order_exception;
    }

    public Integer getIs_reorder() {
        return is_reorder;
    }

    public void setIs_reorder(Integer is_reorder) {
        this.is_reorder = is_reorder;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallback_message() {
        return callback_message;
    }

    public void setCallback_message(String callback_message) {
        this.callback_message = callback_message;
    }
}

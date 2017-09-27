package com.xgame.order.consumer.db.dto;

import java.util.Date;

public class RewardOrderLogMappingDto {
    private String server_id;
    private String uid;
    private Integer item_id; // mapping reward_id
    private Integer item_type;
    private Integer item_count;
    private Integer order_type;
    private Integer is_reorder;
    private String order_id;
    private Date indate;
    private String phone;
    private String id;

    // mapping reward_info
    private String memo;
    private Integer catalog;
    private Integer provider_id;
    private Integer price;
    //不同移动运营商的 cardId 移动|联通|电信
    private String extra;
    private String url;


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

    public Integer getOrder_type() {
        return order_type;
    }

    public void setOrder_type(Integer order_type) {
        this.order_type = order_type;
    }

    public Integer getIs_reorder() {
        return is_reorder;
    }

    public void setIs_reorder(Integer is_reorder) {
        this.is_reorder = is_reorder;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Date getIndate() {
        return indate;
    }

    public void setIndate(Date indate) {
        this.indate = indate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getCatalog() {
        return catalog;
    }

    public void setCatalog(Integer catalog) {
        this.catalog = catalog;
    }

    public Integer getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(Integer provider_id) {
        this.provider_id = provider_id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RewardOrderLogMappingDto{" +
                "server_id='" + server_id + '\'' +
                ", uid='" + uid + '\'' +
                ", item_id=" + item_id +
                ", item_type=" + item_type +
                ", item_count=" + item_count +
                ", order_type=" + order_type +
                ", is_reorder=" + is_reorder +
                ", order_id='" + order_id + '\'' +
                ", indate='" + indate + '\'' +
                ", phone='" + phone + '\'' +
                ", id='" + id + '\'' +
                ", memo='" + memo + '\'' +
                ", catalog=" + catalog +
                ", provider_id=" + provider_id +
                ", price=" + price +
                ", extra="+ extra+
                ", url="+ url+
                '}';
    }
}

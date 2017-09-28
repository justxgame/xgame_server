package com.xgame.order.consumer.rest.model.pmi;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class FuelCardCallBackModel {
    @JSONField(name = "ORDER_NO")
    private String orderNo;
    @JSONField(name = "ORDER_STATUS")
    private String orderStatus;
    @JSONField(name = "RESULT_DATA")
    private List<Card> resultDate;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Card> getResultDate() {
        return resultDate;
    }

    public void setResultDate(List<Card> resultDate) {
        this.resultDate = resultDate;
    }
}

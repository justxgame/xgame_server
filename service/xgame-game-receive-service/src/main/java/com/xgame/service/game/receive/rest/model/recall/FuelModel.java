package com.xgame.service.game.receive.rest.model.recall;


import com.fasterxml.jackson.annotation.JsonProperty;

public class FuelModel {
    @JsonProperty("ORDER_NO")
    private String orderNo;
    @JsonProperty("ORDER_STATUS")
    private String status;

    public String getOrderNo() {
        return orderNo;
    }


    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return "orderNo=" + orderNo + " status=" + status;
    }
}

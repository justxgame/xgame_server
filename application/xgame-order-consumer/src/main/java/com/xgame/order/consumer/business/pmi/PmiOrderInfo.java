package com.xgame.order.consumer.business.pmi;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderinfo")
public class PmiOrderInfo {
    private String msg;
    private Integer reqcode;
    private Integer retcode;
    private String orderId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getRetcode() {
        return retcode;
    }

    public void setRetcode(Integer retcode) {
        this.retcode = retcode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getReqcode() {
        return reqcode;
    }

    public void setReqcode(Integer reqcode) {
        this.reqcode = reqcode;
    }
}

package com.xgame.service.common.conf;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orderinfo")
public class OrderInfo {

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

    public Integer getReqcode() {
        return reqcode;
    }

    public void setReqcode(Integer reqcode) {
        this.reqcode = reqcode;
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
}

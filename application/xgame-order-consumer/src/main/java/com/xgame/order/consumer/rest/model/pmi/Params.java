package com.xgame.order.consumer.rest.model.pmi;

import com.alibaba.fastjson.annotation.JSONField;

public class Params {

    private String ORDER_NO;

    private String CARD_TYPE;

    private String CARD_TYPE_NAME;

    private String CARD_CODE;

    private String PHONE_NO;

    private String RET_URL;

    private String RECHARGE_AMOUNT;

    private String QUANTITY;

    @JSONField(name="ORDER_NO")
    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }
    @JSONField(name="CARD_TYPE")
    public String getCARD_TYPE() {
        return CARD_TYPE;
    }

    public void setCARD_TYPE(String CARD_TYPE) {
        this.CARD_TYPE = CARD_TYPE;
    }
    @JSONField(name="CARD_TYPE_NAME")
    public String getCARD_TYPE_NAME() {
        return CARD_TYPE_NAME;
    }

    public void setCARD_TYPE_NAME(String CARD_TYPE_NAME) {
        this.CARD_TYPE_NAME = CARD_TYPE_NAME;
    }
    @JSONField(name="CARD_CODE")
    public String getCARD_CODE() {
        return CARD_CODE;
    }

    public void setCARD_CODE(String CARD_CODE) {
        this.CARD_CODE = CARD_CODE;
    }
    @JSONField(name="PHONE_NO")
    public String getPHONE_NO() {
        return PHONE_NO;
    }

    public void setPHONE_NO(String PHONE_NO) {
        this.PHONE_NO = PHONE_NO;
    }
    @JSONField(name="RET_URL")
    public String getRET_URL() {
        return RET_URL;
    }

    public void setRET_URL(String RET_URL) {
        this.RET_URL = RET_URL;
    }
    @JSONField(name="RECHARGE_AMOUNT")
    public String getRECHARGE_AMOUNT() {
        return RECHARGE_AMOUNT;
    }

    public void setRECHARGE_AMOUNT(String RECHARGE_AMOUNT) {
        this.RECHARGE_AMOUNT = RECHARGE_AMOUNT;
    }
    @JSONField(name="QUANTITY")
    public String getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(String QUANTITY) {
        this.QUANTITY = QUANTITY;
    }
}

package com.xgame.order.consumer.rest.model.pmi;

import com.alibaba.fastjson.annotation.JSONField;

public class Card {
    @JSONField(name = "CARD_NO")
    private String cardNo;
    @JSONField(name = "CARD_PWD")
    private String cardPwd;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardPwd() {
        return cardPwd;
    }

    public void setCardPwd(String cardPwd) {
        this.cardPwd = cardPwd;
    }
}

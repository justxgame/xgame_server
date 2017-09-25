package com.xgame.order.consumer.rest.model;

public class OrderCard {
    private String cardno;
    private String cardpws;
    private String expiretime;

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getCardpws() {
        return cardpws;
    }

    public void setCardpws(String cardpws) {
        this.cardpws = cardpws;
    }

    public String getExpiretime() {
        return expiretime;
    }

    public void setExpiretime(String expiretime) {
        this.expiretime = expiretime;
    }
}

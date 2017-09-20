package com.xgame.order.consumer.rest.model;

import com.alibaba.fastjson.annotation.JSONField;

public class OutParam {
    private String BUSI_RESULT;
    private String BUSI_RESULT_DESC;

    @JSONField(name="BUSI_RESULT")
    public String getBUSI_RESULT() {
        return BUSI_RESULT;
    }

    public void setBUSI_RESULT(String BUSI_RESULT) {
        this.BUSI_RESULT = BUSI_RESULT;
    }

    @JSONField(name="BUSI_RESULT_DESC")
    public String getBUSI_RESULT_DESC() {
        return BUSI_RESULT_DESC;
    }

    public void setBUSI_RESULT_DESC(String BUSI_RESULT_DESC) {
        this.BUSI_RESULT_DESC = BUSI_RESULT_DESC;
    }
}

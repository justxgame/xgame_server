package com.xgame.order.consumer.rest.model;

public class FuelCardResModel {

    private String reqid;
    private String appid;
    private String code;
    private String result;
    private String result_desc;
    private String result_error_code;
    private String outparam;

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult_desc() {
        return result_desc;
    }

    public void setResult_desc(String result_desc) {
        this.result_desc = result_desc;
    }

    public String getResult_error_code() {
        return result_error_code;
    }

    public void setResult_error_code(String result_error_code) {
        this.result_error_code = result_error_code;
    }

    public String getOutparam() {
        return outparam;
    }

    public void setOutparam(String outparam) {
        this.outparam = outparam;
    }
}

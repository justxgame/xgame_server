package com.xgame.service.load.balance.db.dto;

public class UserLoginDto {
    private String account_id;
    private String account;
    private String uname;
    private Integer type;
    private String session_token;
    private String inviter_uname;
    private String in_date;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }

    public String getInviter_uname() {
        return inviter_uname;
    }

    public void setInviter_uname(String inviter_uname) {
        this.inviter_uname = inviter_uname;
    }

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }
}

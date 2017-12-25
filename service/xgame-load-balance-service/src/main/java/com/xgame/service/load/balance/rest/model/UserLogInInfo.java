package com.xgame.service.load.balance.rest.model;

public class UserLogInInfo {
    private LoginUser u_info;
    private String session_token;

    public LoginUser getU_info() {
        return u_info;
    }

    public void setU_info(LoginUser u_info) {
        this.u_info = u_info;
    }

    public String getSession_token() {
        return session_token;
    }

    public void setSession_token(String session_token) {
        this.session_token = session_token;
    }
}

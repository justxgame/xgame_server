package com.xgame.service.manager.rest.model.response;


import java.util.List;

public class GameSettingList {
    private Integer code;
    private List<GameSettingSendModel> list;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<GameSettingSendModel> getList() {
        return list;
    }

    public void setList(List<GameSettingSendModel> list) {
        this.list = list;
    }
}

package com.xgame.service.manager.rest.model.response;


import com.xgame.service.manager.rest.model.game.GameSettingResModel;


import java.util.List;

public class GameSettingList {
    private Integer code;
    private List<GameSettingResModel> list;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<GameSettingResModel> getList() {
        return list;
    }

    public void setList(List<GameSettingResModel> list) {
        this.list = list;
    }
}

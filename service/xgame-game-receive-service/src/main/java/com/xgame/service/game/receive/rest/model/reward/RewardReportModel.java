package com.xgame.service.game.receive.rest.model.reward;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RewardReportModel {
    @JsonProperty("Server_id")
    private Integer server_id;
    @JsonProperty("Uid")
    private Integer uid;
    @JsonProperty("Id")
    private Integer id;
    @JsonProperty("Type")
    private Integer type;
    @JsonProperty("Count")
    private Integer count;
    @JsonProperty("IsReorder")
    private Integer isReorder;


    public Integer getServer_id() {
        return server_id;
    }


    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getIsReorder() {
        return isReorder;
    }

    public void setIsReorder(Integer isReorder) {
        this.isReorder = isReorder;
    }

    @Override
    public String toString(){
        return "rewardReportModel[server_id=" + server_id + " uid=" + uid + " id=" + id + " type="
                + type + " count=" + count + " isReorder=" + isReorder + "]";
    }
}

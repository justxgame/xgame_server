package com.xgame.service.game.receive.rest.model.reward;

public class RewardInfo {
    private String server_id;
    private String uid;
    private Integer id;
    private Integer type;
    private Integer count;
    private Integer isReorder;

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
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
}

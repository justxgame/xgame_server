package com.xgame.service.manager.rest.model.reward;

public class RewardBoxModel {
    private RewardItemTypeBoxModel itemTypeBoxModel;
    private RewardOrderTypeBoxModel orderTypeBoxModel;

    public RewardItemTypeBoxModel getItemTypeBoxModel() {
        return itemTypeBoxModel;
    }

    public void setItemTypeBoxModel(RewardItemTypeBoxModel itemTypeBoxModel) {
        this.itemTypeBoxModel = itemTypeBoxModel;
    }

    public RewardOrderTypeBoxModel getOrderTypeBoxModel() {
        return orderTypeBoxModel;
    }

    public void setOrderTypeBoxModel(RewardOrderTypeBoxModel orderTypeBoxModel) {
        this.orderTypeBoxModel = orderTypeBoxModel;
    }
}

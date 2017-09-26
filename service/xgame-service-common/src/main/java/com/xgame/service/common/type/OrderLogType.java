package com.xgame.service.common.type;

/**
 * Created by william on 2017/9/26.
 */
public enum OrderLogType {

    Consumer(1),  // 订单已被消费
    NoConsumer(0); // 订单未被消费

    private Integer value;

    OrderLogType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}

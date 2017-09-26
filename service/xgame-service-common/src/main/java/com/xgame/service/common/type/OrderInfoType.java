package com.xgame.service.common.type;

/**
 * Created by william on 2017/9/26.
 *
 * 订单状态  1失败， 0=成功    2=初始化，3=充值中
 */
public enum  OrderInfoType {

    FAILURE(1),  // 失败
    SUCCESS(0), // 成功
    INIT(2), // 初始化
    CHARGING(3); // 充值中

    private Integer value;

    OrderInfoType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}

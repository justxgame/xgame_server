package com.xgame.order.consumer.db.dao;


import org.springframework.stereotype.Component;

@Component
public interface SubOrderInfoDao extends BaseDao{

    Integer countStateByOrderId(String order_id, Integer state);

}

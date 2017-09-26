package com.xgame.order.consumer.db.dao;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface RewardOrderInfoDao extends BaseDao{

    /**
     * 根据orderId获取 server_url
     *
     * @param order_id
     * @return
     */
   String getServerUrlByOrderId(String order_id);

}

package com.xgame.order.consumer.db.dao;


import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface RewardOrderLogMappingDao extends BaseDao{

    List<RewardOrderLogMappingDto> getAllOrdersLogByOrderType(int orderType);

    void updateOrderToConsumer(String order_id);

}

package com.xgame.order.consumer.service;

import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogMappingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by william on 2017/9/26.
 */
@Service
public class OrderService {


    @Autowired
    private RewardOrderInfoDao rewardOrderInfoDao;

    @Autowired
    private RewardOrderLogMappingDao rewardOrderLogMappingDao;


    public RewardOrderInfoDao getRewardOrderInfoDao() {
        return rewardOrderInfoDao;
    }

    public RewardOrderLogMappingDao getRewardOrderLogMappingDao() {
        return rewardOrderLogMappingDao;
    }
}

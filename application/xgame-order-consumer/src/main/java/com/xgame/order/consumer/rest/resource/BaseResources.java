package com.xgame.order.consumer.rest.resource;


import com.xgame.order.consumer.ServiceContextFactory;
import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.SubOrderInfoDao;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

public class BaseResources {

    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;


    protected RewardOrderInfoDao rewardOrderInfoDao = ServiceContextFactory.getRewardOrderInfoDao();
    protected SubOrderInfoDao subOrderInfoDao = ServiceContextFactory.getSubOrderInfoDao();

    protected final static String HTTP_PREFIX= "http://";

}

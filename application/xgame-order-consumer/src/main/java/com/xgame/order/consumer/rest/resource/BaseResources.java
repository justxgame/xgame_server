package com.xgame.order.consumer.rest.resource;


import com.xgame.order.consumer.ServiceContextFactory;
import com.xgame.order.consumer.business.ofpay.AbstractOfPayBusiness;
import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.SubOrderInfoDao;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.SubOrderInfoDto;
import com.xgame.order.consumer.rest.model.ExchangeResultModel;
import com.xgame.service.common.type.OrderInfoType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

public class BaseResources {

    private static Logger logger = LoggerFactory.getLogger(BaseResources.class.getName());
    protected static final String PHONE_CHARGE_SUCCESS = "话费充值成功";

    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;


    protected RewardOrderInfoDao rewardOrderInfoDao = ServiceContextFactory.getRewardOrderInfoDao();
    protected SubOrderInfoDao subOrderInfoDao = ServiceContextFactory.getSubOrderInfoDao();

    protected final static String HTTP_PREFIX = "http://";


    protected void validatorSubOrder(String orderId, RewardOrderInfoDto rewardOrderInfoDto, SubOrderInfoDto subOrderInfoDto,String pwd) {
        //更新order表
        rewardOrderInfoDao.updateObjectById(rewardOrderInfoDto);
        //保存sub order表
        subOrderInfoDao.saveObject(subOrderInfoDto);

        //对父订单进行判断，是否已经全部成功
        Integer successSubOrderNumber = subOrderInfoDao.countStateByOrderId(orderId, OrderInfoType.SUCCESS.getValue());
        logger.info("[validatorSubOrder] order_id=" + orderId + ", successSubOrderNumber= " + successSubOrderNumber + ", total is " + rewardOrderInfoDto.getItem_count());

        // 已经全部成功
        if (successSubOrderNumber >= rewardOrderInfoDto.getItem_count()) {
            logger.info("[validatorSubOrder] all successful , send order_id to game server ,order_id = " + orderId);
            String serverUrl = rewardOrderInfoDao.getServerUrlByOrderId(orderId);
            if (StringUtils.isEmpty(serverUrl)) {
                throw new RuntimeException("server url is empty");
            }
            if (null == rewardOrderInfoDto) {
                throw new RuntimeException("rewardOrderInfoDto is not exist , order_Id = " + orderId);
            }
            rewardOrderInfoDto.setOrder_status(OrderInfoType.SUCCESS.getValue());
            try {
                // callback
                //回调游戏服务商
                //生成 model
                ExchangeResultModel exchangeResultModel = new ExchangeResultModel();
                exchangeResultModel.setUid(Integer.parseInt(rewardOrderInfoDto.getUid()));
                exchangeResultModel.setServerId(Integer.valueOf(rewardOrderInfoDto.getServer_id()));
                exchangeResultModel.setId(Integer.valueOf(rewardOrderInfoDto.getId()));
                exchangeResultModel.setPassword(pwd);
                String gameUrl = HTTP_PREFIX + serverUrl + "/exchange_result";
                logger.info("[validatorSubOrder] call url = " + gameUrl + ", exchangeResultModel=" + exchangeResultModel);
                AbstractOfPayBusiness.gameCallBack(gameUrl, exchangeResultModel);
                rewardOrderInfoDto.setMessage(rewardOrderInfoDto.getMessage() + ",  game call back success");
            } catch (Throwable t) {
                rewardOrderInfoDto.setMessage(rewardOrderInfoDto.getMessage() + ",  game call back failed");
            }
            rewardOrderInfoDao.updateObjectById(rewardOrderInfoDto);
        }
    }

}

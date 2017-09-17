package com.xgame.service.game.receive.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.game.receive.db.dto.RewardOrderDto;
import com.xgame.service.game.receive.rest.model.reward.RewardInfo;
import com.xgame.service.game.receive.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/exchange")
public class ExchangeResources extends BaseResources{
    private static Logger logger = LoggerFactory.getLogger(ExchangeResources.class.getName());

    @POST
    @Path("/reward_receive")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getOrderInfo(RewardInfo rewardInfo){

        logger.info("receive");
        //TODO check param && async use queue
        RewardOrderDto orderInfo = getRewardOrder(rewardInfo);

        rewardOrderService.saveObject(orderInfo);

        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setCode(successCode);
        return wrapResponseModel;
    }
    private RewardOrderDto getRewardOrder(RewardInfo rewardInfo){
        RewardOrderDto orderInfo =new RewardOrderDto();
        orderInfo.setServerId(rewardInfo.getServer_id());
        orderInfo.setUid(rewardInfo.getUid());
        orderInfo.setItemId(rewardInfo.getId());
        orderInfo.setItemType(rewardInfo.getType());
        orderInfo.setItemCount(rewardInfo.getType());
        orderInfo.setOrderType(0);
        orderInfo.setIsReorder(rewardInfo.getIsReorder());
        String orderId = CommonUtils.getOrderId(1);
        orderInfo.setOrderId(orderId);
        orderInfo.setIndate(CommonUtils.getFormatDateByNow());
        return orderInfo;
    }



}

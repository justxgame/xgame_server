package com.xgame.service.game.receive.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.game.receive.db.dto.RewardOrderDto;
import com.xgame.service.game.receive.rest.model.reward.RewardReportModel;
import com.xgame.service.game.receive.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/exchange")
public class ExchangeResources extends BaseResources{
    private static Logger logger = LoggerFactory.getLogger(ExchangeResources.class.getName());

    @POST
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getOrderInfo(String  str){

        logger.info("receive");

        logger.info("model"+ str);
        //TODO check param && async use queue
        RewardReportModel rewardReportModel = JSONObject.parseObject(str, RewardReportModel.class);
        RewardOrderDto orderInfo = getRewardOrder(rewardReportModel);

        rewardOrderService.saveObject(orderInfo);

        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setCode(successCode);

        return wrapResponseModel;
    }
    private RewardOrderDto getRewardOrder(RewardReportModel rewardReportModel){
        RewardOrderDto orderInfo =new RewardOrderDto();
        orderInfo.setServerId(String.valueOf(rewardReportModel.getServer_id()));
        orderInfo.setUid(String.valueOf(rewardReportModel.getUid()));
        orderInfo.setItemId(rewardReportModel.getId());
        orderInfo.setItemType(rewardReportModel.getType());
        orderInfo.setItemCount(rewardReportModel.getType());
        orderInfo.setOrderType(0);
        orderInfo.setIsReorder(rewardReportModel.getIsReorder());
        String orderId = CommonUtils.getOrderId(1);
        orderInfo.setOrderId(orderId);
        orderInfo.setIndate(CommonUtils.getFormatDateByNow());
        return orderInfo;
    }



}

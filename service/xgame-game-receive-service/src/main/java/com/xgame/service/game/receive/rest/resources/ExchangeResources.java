package com.xgame.service.game.receive.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.game.receive.db.dto.RewardOrderDto;
import com.xgame.service.game.receive.rest.model.reward.RewardReportModel;
import com.xgame.service.game.receive.util.CommonUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
    public WrapResponseModel getOrderInfo(RewardReportModel  model){

        logger.info("receive");
        WrapResponseModel wrapResponseModel = new WrapResponseModel();

        logger.info("mode"+model);
        RewardOrderDto orderInfo = getRewardOrder(model);

        try {
            rewardOrderService.saveObject(orderInfo);
            wrapResponseModel.setCode(successCode);
        }catch (Throwable t){
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }



        wrapResponseModel.setCode(successCode);

        return wrapResponseModel;
    }
    private RewardOrderDto getRewardOrder(RewardReportModel rewardReportModel){
        RewardOrderDto orderInfo =new RewardOrderDto();
        orderInfo.setServerId(String.valueOf(rewardReportModel.getServer_id()));
        orderInfo.setUid(String.valueOf(rewardReportModel.getUid()));
        orderInfo.setItemId(rewardReportModel.getItemId());
        orderInfo.setId(rewardReportModel.getId());
        orderInfo.setItemType(rewardReportModel.getType());
        orderInfo.setItemCount(rewardReportModel.getType());
        orderInfo.setOrderType(0);
        orderInfo.setIsReorder(rewardReportModel.getIsReorder());
        orderInfo.setPhone(rewardReportModel.getPhone());
        String orderId = CommonUtils.getOrderId(1);
        orderInfo.setOrderId(orderId);
        orderInfo.setIndate(CommonUtils.getFormatDateByNow());
        orderInfo.setOpType(rewardReportModel.getOpType());
        return orderInfo;
    }



}

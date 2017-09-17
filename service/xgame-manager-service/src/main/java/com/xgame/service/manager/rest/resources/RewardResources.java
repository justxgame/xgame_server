package com.xgame.service.manager.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.rest.model.reward.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/reward")
public class RewardResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(RewardResources.class.getName());
    @GET
    @Path("/getAllBox")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAllBox(){
        logger.info("getAllBox");
        WrapResponseModel responseModel = new WrapResponseModel();

        RewardItemTypeBoxModel itemTypeNavModel = new RewardItemTypeBoxModel();
        List<RewardItemTypeModel> rewardItemTypeModels = new ArrayList<>();
        RewardItemTypeModel itemTypeModel = new RewardItemTypeModel();
        itemTypeModel.setItemTypeId(1);
        itemTypeModel.setItemTypeName("50加油卡");
        RewardItemTypeModel itemTypeModel2 = new RewardItemTypeModel();
        itemTypeModel2.setItemTypeId(2);
        itemTypeModel2.setItemTypeName("100加油卡");
        rewardItemTypeModels.add(itemTypeModel);
        rewardItemTypeModels.add(itemTypeModel2);
        itemTypeNavModel.setItemTypeModelList(rewardItemTypeModels);

        RewardOrderTypeBoxModel orderTypeNavModel = new RewardOrderTypeBoxModel();
        List<RewardOrderTypeModel> orderTypeModels = new ArrayList<>();
        RewardOrderTypeModel orderTypeModel1 = new RewardOrderTypeModel();
        orderTypeModel1.setOrderTypeId(1);
        orderTypeModel1.setOrderTypeName("成功");
        RewardOrderTypeModel orderTypeModel2 = new RewardOrderTypeModel();
        orderTypeModel2.setOrderTypeId(2);
        orderTypeModel2.setOrderTypeName("失败");

        orderTypeModels.add(orderTypeModel1);
        orderTypeModels.add(orderTypeModel2);

        orderTypeNavModel.setOrderTypeModelList(orderTypeModels);

        RewardBoxModel boxModel = new RewardBoxModel();
        boxModel.setItemTypeBoxModel(itemTypeNavModel);
        boxModel.setOrderTypeBoxModel(orderTypeNavModel);

        responseModel.setData(boxModel);
        responseModel.setCode(successCode);
        return responseModel;
    }



    @GET
    @Path("/getRewardOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getRewardOrderInfo(@QueryParam("rewardType")Integer rewardType,@QueryParam("orderType")Integer orderType,
          @QueryParam("dateFrom")Long dateFrom,@QueryParam("dateTo")Long dateTo){
        logger.info("getRewardOrder");
        WrapResponseModel responseModel = new WrapResponseModel();

        List<RewardOrderModel> rewardOrderModelList = new ArrayList<>();
        RewardOrderModel orderModel1 = new RewardOrderModel();
        orderModel1.setAddress("上海");
        orderModel1.setException("");
        orderModel1.setServerId("10.1.1.1");
        orderModel1.setUid(1111);
        orderModel1.setRewardType(1);
        orderModel1.setRewardName("50加油卡");
        orderModel1.setRewardCount(1);
        orderModel1.setPhoneNo("18817333328");
        orderModel1.setOrderId("001");
        orderModel1.setIndate(System.currentTimeMillis());
        orderModel1.setOrderType(1);
        orderModel1.setSupplierOrderId("001");
        rewardOrderModelList.add(orderModel1);

        RewardOrderModel orderModel2 = new RewardOrderModel();
        orderModel2.setAddress("上海");
        orderModel2.setException("");
        orderModel2.setServerId("10.1.1.1");
        orderModel2.setUid(1111);
        orderModel2.setRewardType(1);
        orderModel2.setRewardName("50加油卡");
        orderModel2.setRewardCount(1);
        orderModel2.setPhoneNo("18817333328");
        orderModel2.setOrderId("001");
        orderModel2.setIndate(System.currentTimeMillis());
        orderModel2.setOrderType(1);
        orderModel2.setSupplierOrderId("001");
        rewardOrderModelList.add(orderModel2);



        responseModel.setData(rewardOrderModelList);
        responseModel.setCode(successCode);
        return responseModel;
    }


    @POST
    @Path("/sendReCallOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel sendRecallOrder(RewardOrderModel orderModel) {
        logger.info("sendReCallOrder");
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setCode(successCode);
        return wrapResponseModel;
    }
}

package com.xgame.service.manager.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.manager.ServiceConfiguration;
import com.xgame.service.manager.db.dto.RewardBoxDto;
import com.xgame.service.manager.db.dto.RewardOrderDetailDto;
import com.xgame.service.manager.rest.model.reward.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/reward")
public class RewardResources extends BaseResources {

    private static Logger logger = LoggerFactory.getLogger(RewardResources.class.getName());
    @GET
    @Path("/getAllBox")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAllBox(){
        String uid = getUid();
        logger.info("[RewardResources] user "+uid+ "get all box");
        WrapResponseModel responseModel = new WrapResponseModel();

        try {
            List<RewardBoxDto> dtos = rewardBoxService.getAll();
            List<RewardBoxDto> dtos2 = new ArrayList<>();
            RewardBoxDto rewardBoxDto = new RewardBoxDto();
            rewardBoxDto.setId("all");
            rewardBoxDto.setMemo("不限");
            dtos2.add(rewardBoxDto);
            for (RewardBoxDto dto:dtos){
                dtos2.add(dto);
            }
            RewardItemTypeBoxModel itemTypeNavModel = new RewardItemTypeBoxModel();

            List<RewardItemTypeModel> rewardItemTypeModels = parseRewardBoxDto2Model(dtos2);

            itemTypeNavModel.setItemTypeModelList(rewardItemTypeModels);

            RewardOrderTypeBoxModel orderTypeNavModel = new RewardOrderTypeBoxModel();
            List<RewardOrderTypeModel> orderTypeModels = getRewardOrderTypeModels();

            orderTypeNavModel.setOrderTypeModelList(orderTypeModels);

            RewardBoxModel boxModel = new RewardBoxModel();
            boxModel.setItemTypeBoxModel(itemTypeNavModel);
            boxModel.setOrderTypeBoxModel(orderTypeNavModel);

            responseModel.setData(boxModel);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.info("[RewardResources] user get all box error"+ExceptionUtils.getMessage(t));
        }

        return responseModel;
    }



    @GET
    @Path("/getRewardOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getRewardOrderInfo(@QueryParam("rewardType")String rewardType,@QueryParam("orderType")Integer orderType,
          @QueryParam("dateFrom")Long dateFrom,@QueryParam("dateTo")Long dateTo){
        String uid = getUid();
        String op = "[RewardResources] user "+uid+" get order info.Parames rewardType="+rewardType+" orderType="+
                orderType+" dateFrom="+dateFrom+" dateTo="+dateTo;

        operationLog(uid,op);
        Map<String, Object> query = new HashMap<>();
        query.put("rewardType",rewardType);
        query.put("orderType", orderType);
        if (dateFrom==0&&dateTo==0){
            //1970
            dateFrom=838861000l;
            //2100
            dateTo=4103283661000l;
        }

        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            query.put("dateFrom", CommonUtil.getDsFromUnixTimestamp(dateFrom));
            query.put("dateTo", CommonUtil.getDsFromUnixTimestamp(dateTo));
            List<RewardOrderDetailDto> dtos=rewardOrderDetailService.getAllByQuery(query);
            List<RewardOrderModel> models = parseRewardOrderDetailDto2Model(dtos);
            responseModel.setData(models);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            logger.info("[RewardResources] getRewardOrder error"+ExceptionUtils.getMessage(t));
        }


        return responseModel;
    }


    @POST
    @Path("/sendReCallOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel sendRecallOrder(RewardOrderModel orderModel) {
        logger.info("[RewardResources] send Recallorder");
        WrapResponseModel wrapResponseModel = new WrapResponseModel();

        String uid = getUid();
        String op = "[RewardResources] sendRecall order. orderid:"+orderModel.getOrderId();
        operationLog(uid,op);
//        String receiveUrl = ServiceConfiguration.getInstance().getConfig().getString("xgame.receive.service.url");
//        HttpPost post = new HttpPost(receiveUrl);
//        RewardReportModel rewardReportModel = new RewardReportModel();
//        rewardReportModel.setUid(orderModel.getUid());
//        rewardReportModel.setServer_id(Integer.valueOf(orderModel.getServerId()));
//        rewardReportModel.setIsReorder(1);
//        rewardReportModel.setCount(orderModel.getRewardCount());
//        rewardReportModel.setId(orderModel.getRewardId());
//        rewardReportModel.setType(orderModel.getRewardType());
//        String jsonString = JSONObject.toJSONString(rewardReportModel);
//        StringEntity reqEntity = new StringEntity(jsonString, Charset.forName("UTF-8"));
//        reqEntity.setContentEncoding("UTF-8");
//        reqEntity.setContentType("application/json");
//
//        post.setEntity(reqEntity);
//        CloseableHttpResponse response =null;
//        HttpEntity entity=null;
//        try {
//            response = httpclient.execute(post);
//            entity = response.getEntity();
//            String res = EntityUtils.toString(entity, "UTF-8");
//            WrapResponseModel responseModel = JSONObject.parseObject(res, WrapResponseModel.class);
//            if (successCode==responseModel.getCode()){
//                wrapResponseModel.setCode(successCode);
//            }else {
//                wrapResponseModel.setCode(errorCode);
//                wrapResponseModel.setMessage(responseModel.getMessage());
//            }
//
//        } catch (Throwable t) {
//            wrapResponseModel.setCode(errorCode);
//            wrapResponseModel.setMessage(ExceptionUtils.getStackTrace(t));
//
//        }finally {
//            try {
//                response.close();
//                EntityUtils.consume(entity);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        //修改订单状态
//        if (wrapResponseModel.getCode()!=errorCode){
//            String order_id =orderModel.getOrderId();
//            try {
//                rewardOrderDetailService.updateRecalorder(order_id);
//            }catch (Throwable t){
//                wrapResponseModel.setCode(errorCode);
//                wrapResponseModel.setMessage("update  order status error"+ExceptionUtils.getMessage(t));
//            }
//
//        }
        wrapResponseModel.setCode(successCode);


        return wrapResponseModel;
    }


    private List<RewardOrderTypeModel> getRewardOrderTypeModels(){
        List<RewardOrderTypeModel> orderTypeModels = new ArrayList<>();

        RewardOrderTypeModel orderTypeModel3 = new RewardOrderTypeModel();
        orderTypeModel3.setOrderTypeId(4);
        orderTypeModel3.setOrderTypeName("不限");
        orderTypeModels.add(orderTypeModel3);

        RewardOrderTypeModel orderTypeModel1 = new RewardOrderTypeModel();
        orderTypeModel1.setOrderTypeId(0);
        orderTypeModel1.setOrderTypeName("成功");
        orderTypeModels.add(orderTypeModel1);

        RewardOrderTypeModel orderTypeModel2 = new RewardOrderTypeModel();
        orderTypeModel2.setOrderTypeId(1);
        orderTypeModel2.setOrderTypeName("失败");
        orderTypeModels.add(orderTypeModel2);

        RewardOrderTypeModel orderTypeModel4 = new RewardOrderTypeModel();
        orderTypeModel4.setOrderTypeId(2);
        orderTypeModel4.setOrderTypeName("初始化");
        orderTypeModels.add(orderTypeModel4);

        RewardOrderTypeModel orderTypeModel6 = new RewardOrderTypeModel();
        orderTypeModel6.setOrderTypeId(3);
        orderTypeModel6.setOrderTypeName("充值中");
        orderTypeModels.add(orderTypeModel6);

        RewardOrderTypeModel orderTypeModel5 = new RewardOrderTypeModel();
        orderTypeModel5.setOrderTypeId(9);
        orderTypeModel5.setOrderTypeName("撤销");
        orderTypeModels.add(orderTypeModel5);


        return orderTypeModels;
    }

}

package com.xgame.service.manager.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.manager.ServiceConfiguration;
import com.xgame.service.manager.db.dto.BroadCastDto;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.broadcast.BroadCastModel;
import com.xgame.service.manager.rest.model.broadcast.BroadCastSendModel;
import com.xgame.service.manager.rest.model.response.ServerRes;
import com.xgame.service.manager.rest.model.server.ServerBoxModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Path("/broadcast")
public class BroadcastResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(BroadcastResources.class.getName());

    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel sendBroadCast(BroadCastModel broadCastModel){
        logger.info("[BroadcastResources]send:");
        WrapResponseModel responseModel = new WrapResponseModel();
        String uid = getUid();
        String op = "[BroadcastResources] send msg[" + broadCastModel.getMessage() + "] to server " + broadCastModel.getServerId();

        try {
            String token = requestContext.getHeaderString("token");
            String userName = tokenService.getUserNameByToken(token);
            BroadCastDto broadCastDto = parseBroadcastMode2Dto(broadCastModel,userName);
            List<ServerStatusDto> dtos = statusService.getAll();


            if ("all".equalsIgnoreCase(broadCastModel.getServerId())){
                for (ServerStatusDto dto:dtos){
                    sendBroadcast(dto, broadCastModel.getMessage());
                    broadcastService.saveObject(broadCastDto);
                }
                operationLog(uid,op);
                responseModel.setCode(successCode);
                return responseModel;
            }
            ServerStatusDto dto = getDtoById(broadCastModel.getServerId(), dtos);
            if (dto==null){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Can't find server by id "+broadCastModel.getServerId() +" please make sure server exist");
                return responseModel;
            }
            sendBroadcast(dto, broadCastModel.getMessage());
            broadcastService.saveObject(broadCastDto);
            operationLog(uid,op);
            responseModel.setCode(successCode);


        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }

        return responseModel;
    }

    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getHistory(@QueryParam("serverId")String serverId){
        logger.info("history");
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            List<BroadCastDto> broadCastDtos = broadcastService.getAll();
            List<BroadCastModel> models = new ArrayList<>();
            for (BroadCastDto dto:broadCastDtos){
                BroadCastModel model = parseBroadcastMode2Dto(dto);
                models.add(model);
            }
            responseModel.setCode(successCode);
            responseModel.setData(models);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }

//        BroadCastModel model1 = new BroadCastModel();
//        model1.setMessage("祝贺 xx 获得 xxx");
//        model1.setSendDate(System.currentTimeMillis());
//        model1.setServerId("10.1.1.1");
//        model1.setSendUserName("admin");
//        broadCastModels.add(model1);
//        BroadCastModel model2 = new BroadCastModel();
//        model2.setMessage("祝贺 xx 获得 xxx");
//        model2.setSendDate(System.currentTimeMillis());
//        model2.setServerId("10.1.1.2");
//        model2.setSendUserName("admin2");
//        broadCastModels.add(model2);
//        responseModel.setData(broadCastModels);

        return responseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(BroadCastModel model){
        WrapResponseModel responseModel = new WrapResponseModel();
        String uid = getUid();
        String op = "[BroadcastResources] delete msg .serverid:" + model.getServerId() + " msg:" + model.getMessage();
        try {
            broadcastService.deleteMsg(model.getTransection());

            responseModel.setCode(successCode);

        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }
        return responseModel;
    }

    private void sendBroadcast(ServerStatusDto dto,String message) throws IOException {

        String sendUrl = HTTP_PREFIX+dto.getUrl()+"/broadcast";
        HttpPost post = new HttpPost(sendUrl);
        BroadCastSendModel sendModel = new BroadCastSendModel();
        sendModel.setServer_id(dto.getServer_id());

        sendModel.setMsg(message);
        String jsonStr = JSONObject.toJSONString(sendModel);
        StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
        reqEntity.setContentEncoding("UTF-8");
        reqEntity.setContentType("application/json");

        post.setEntity(reqEntity);
        CloseableHttpResponse response =null;
        HttpEntity entity=null;
        try {
            response = httpclient.execute(post);

            entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            ServerRes serverRes = JSONObject.parseObject(res,ServerRes.class);
            if (serverRes.getCode()!=0){

               logger.error("send broadcast error by game server get error");
            }


        }finally {
            try {
                response.close();
                EntityUtils.consume(entity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BroadCastDto parseBroadcastMode2Dto(BroadCastModel model,String userName){
        BroadCastDto dto = new BroadCastDto();
        dto.setTransection(model.getTransection());
        dto.setIndate(CommonUtil.getFormatDateByNow());
        dto.setMsg(model.getMessage());
        dto.setSend_user(userName);
        dto.setServer_id(model.getServerId());
        return dto;
    }
    private BroadCastModel parseBroadcastMode2Dto(BroadCastDto dto){
        BroadCastModel model = new BroadCastModel();
        model.setServerId(dto.getServer_id());
        model.setMessage(dto.getMsg());
        model.setSendUserName(dto.getSend_user());
        model.setTransection(dto.getTransection());
        model.setSendDate(CommonUtil.parseStr2Time(dto.getIndate()));
        return model;
    }

}

package com.xgame.service.manager.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.ServiceConfiguration;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.broadcast.BroadCastModel;
import com.xgame.service.manager.rest.model.broadcast.BroadCastSendModel;
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
        logger.info("send");
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            List<ServerStatusDto> dtos = statusService.getAll();
            ServerStatusDto dto = getDtoById(broadCastModel.getServerId(), dtos);
            if (dto==null){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Can't find server by id "+broadCastModel.getServerId() +" please make sure server exist");
                return responseModel;
            }
            String sendUrl = dto.getUrl()+"/broadcast";
            HttpPost post = new HttpPost(sendUrl);
            BroadCastSendModel sendModel = new BroadCastSendModel();
            sendModel.setServer_id(Integer.valueOf(broadCastModel.getServerId()));
            sendModel.setMsg(broadCastModel.getMessage());
            String jsonStr = JSONObject.toJSONString(sendModel);
            StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
            reqEntity.setContentEncoding("UTF-8");
            reqEntity.setContentType("application/json");

            post.setEntity(reqEntity);
            CloseableHttpResponse response =null;
            HttpEntity entity=null;
            try {
                response = httpclient.execute(post);
                int code=response.getStatusLine().getStatusCode();
                entity = response.getEntity();
                String res = EntityUtils.toString(entity, "UTF-8");
                if (200==code){
                    responseModel.setCode(successCode);
                }else {
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("call server error.code:"+code+" ."+res);
                }


            }catch (Throwable t){
                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));

            }finally {
                try {
                    response.close();
                    EntityUtils.consume(entity);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        List<BroadCastModel> broadCastModels = new ArrayList<>();
        BroadCastModel model1 = new BroadCastModel();
        model1.setMessage("祝贺 xx 获得 xxx");
        model1.setSendDate(System.currentTimeMillis());
        model1.setServerId("10.1.1.1");
        model1.setSendUserName("admin");
        broadCastModels.add(model1);
        BroadCastModel model2 = new BroadCastModel();
        model2.setMessage("祝贺 xx 获得 xxx");
        model2.setSendDate(System.currentTimeMillis());
        model2.setServerId("10.1.1.2");
        model2.setSendUserName("admin2");
        broadCastModels.add(model2);
        responseModel.setData(broadCastModels);

        responseModel.setCode(successCode);
        return responseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(BroadCastModel model){
        WrapResponseModel responseModel = new WrapResponseModel();
        responseModel.setCode(successCode);
        return responseModel;
    }

}

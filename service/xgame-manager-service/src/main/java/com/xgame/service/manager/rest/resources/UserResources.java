package com.xgame.service.manager.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.ServiceConfiguration;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.user.UserBanModel;
import com.xgame.service.manager.rest.model.user.UserInfoModel;
import com.xgame.service.manager.rest.model.user.UserSearchModel;
import com.xgame.service.manager.rest.model.user.UserUpdateModel;
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

@Path("/user")
public class UserResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(UserResources.class.getName());
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel searchUser(@QueryParam("serverId")String serverId,@QueryParam("uid")String uid,
                                        @QueryParam("userName")String userName){
        logger.info("search user");
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            List<ServerStatusDto> dtos = statusService.getAll();
            ServerStatusDto dto = getDtoById(serverId, dtos);
            if (dto==null){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist");
                return responseModel;
            }
            String sendUrl = dto.getUrl()+"/get_player_info";
            HttpPost post = new HttpPost(sendUrl);
            UserSearchModel searchModel = new UserSearchModel();
            searchModel.setServer_id(Integer.valueOf(serverId));
            searchModel.setUid(Integer.valueOf(uid));
            String jsonStr = JSONObject.toJSONString(searchModel);
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
                logger.info("call server response:"+res);
                responseModel.setCode(successCode);
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

        }

        List<UserInfoModel> userInfoModels = new ArrayList<>();
        UserInfoModel userInfoModel =new UserInfoModel();
        userInfoModel.setServerId("999");
        userInfoModel.setUserName(userName);
        userInfoModel.setUid("1");
        userInfoModel.setMoney(100);
        userInfoModel.setTicket(1000);
        userInfoModel.setActionId(0);
        userInfoModel.setStatus(1);
        userInfoModel.setPoints(10);
        userInfoModel.setCoins(10);

        userInfoModels.add(userInfoModel);
        responseModel.setCode(successCode);
        responseModel.setData(userInfoModels);

        return responseModel;
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateUser(List<UserInfoModel> userInfoModels){
        logger.info("user update");
        UserInfoModel userInfoModel = userInfoModels.get(0);
        int actionId = userInfoModel.getActionId();
        String serverId = userInfoModel.getServerId();
        WrapResponseModel responseModel = new WrapResponseModel();
        List<ServerStatusDto> dtos = statusService.getAll();
        ServerStatusDto dto = getDtoById(serverId, dtos);
        if (dto==null){
            responseModel.setCode(errorCode);
            responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist");
            return responseModel;
        }
        String sendUrl = dto.getUrl();

        String jsonStr =null;
        if (1==actionId||2==actionId){
            sendUrl=sendUrl+"/ban";
            UserBanModel banModel = new UserBanModel();
            banModel.setServer_id(Integer.valueOf(userInfoModel.getServerId()));
            banModel.setUid(Integer.valueOf(userInfoModel.getUid()));
            banModel.setOp_code(actionId);
            jsonStr = JSONObject.toJSONString(banModel);
        }else if(3==actionId||4==actionId){
            if (3==actionId){
                sendUrl=sendUrl+"/alter_player_info";
            }else {
                sendUrl=sendUrl+"/mail";
            }

            UserUpdateModel userUpdateModel = new UserUpdateModel();
            userUpdateModel.setUid(Integer.valueOf(userInfoModel.getUid()));
            userUpdateModel.setServer_id(Integer.valueOf(userInfoModel.getServerId()));
            userUpdateModel.setCoins(userInfoModel.getCoins());
            userUpdateModel.setMoney(userInfoModel.getMoney());
            userUpdateModel.setTicket(userInfoModel.getTicket());
            userUpdateModel.setPoints(userInfoModel.getPoints());
            jsonStr = JSONObject.toJSONString(userUpdateModel);
        }
        StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
        reqEntity.setContentEncoding("UTF-8");
        reqEntity.setContentType("application/json");
        HttpPost post = new HttpPost(sendUrl);
        post.setEntity(reqEntity);
        CloseableHttpResponse response =null;
        HttpEntity entity=null;
        try {
            response = httpclient.execute(post);
            entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            logger.info("call server response:"+res);
            responseModel.setCode(successCode);
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

        responseModel.setCode(successCode);
        return responseModel;
    }
}

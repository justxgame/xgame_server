package com.xgame.service.manager.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.ServiceConfiguration;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.response.ServerRes;
import com.xgame.service.manager.rest.model.response.UserRes;
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
    public WrapResponseModel searchUser(@QueryParam("serverId")String serverId,@QueryParam("userId")String uid,
                                        @QueryParam("userName")String userName){
        logger.info("search user");
        logger.info("serverId:"+serverId+" userId"+uid);
        WrapResponseModel responseModel = new WrapResponseModel();
        ServerStatusDto dto =null;
        if (null==uid) {
            if (null == uid) {
                responseModel.setCode(errorCode);
                responseModel.setMessage("uid is null");
                return responseModel;
            }

        }
        try {
            List<ServerStatusDto> dtos = statusService.getAll();

            if("all".equalsIgnoreCase(serverId)){
                for(ServerStatusDto statusDto:dtos){
                    dto = getDtoById(String.valueOf(statusDto.getServer_id()), dtos);
                    if (dto==null){
                        continue;
                    }
                    UserRes userRes = getUserRes(statusDto, uid);
                    if (null!=userRes){

                        responseModel.setData(userRes);
                        responseModel.setCode(successCode);
                        return responseModel;

                    }
                }
                responseModel.setCode(errorCode);
                responseModel.setMessage("cant find user");
                return responseModel;

            }
            dto = getDtoById(serverId, dtos);
            if (dto==null){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist");
                return responseModel;
            }
            String sendUrl = HTTP_PREFIX+dto.getUrl()+"/get_player_info";
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
                logger.info("game res:"+res);
                if(res!=null){
                    UserRes userRes =JSONObject.parseObject(res, UserRes.class);
                    responseModel.setData(userRes);
                    responseModel.setCode(successCode);
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

//        List<UserInfoModel> userInfoModels = new ArrayList<>();
//        UserInfoModel userInfoModel =new UserInfoModel();
//        userInfoModel.setServerId("999");
//        userInfoModel.setUserName(userName);
//        userInfoModel.setUid("1");
//        userInfoModel.setMoney(100);
//        userInfoModel.setTicket(1000);
//        userInfoModel.setActionId(0);
//        userInfoModel.setStatus(1);
//        userInfoModel.setPoints(10);
//        userInfoModel.setCoins(10);
//
//        userInfoModels.add(userInfoModel);
//        responseModel.setCode(successCode);
//        responseModel.setData(userInfoModels);

        return responseModel;
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateUser(List<UserInfoModel> userInfoModels){
        logger.info("user update");
        UserInfoModel userInfoModel = userInfoModels.get(0);
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            int actionId = userInfoModel.getActionId();
            String serverId = userInfoModel.getServerId();

            List<ServerStatusDto> dtos = statusService.getAll();
            ServerStatusDto dto = getDtoById(serverId, dtos);
            if (dto==null){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist");
                return responseModel;
            }
            String sendUrl = HTTP_PREFIX+dto.getUrl();

            String jsonStr =null;
            if (1==actionId||2==actionId){
                sendUrl=sendUrl+"/ban";
                logger.info("sendurl "+sendUrl);
                UserBanModel banModel = new UserBanModel();
                banModel.setServer_id(Integer.valueOf(userInfoModel.getServerId()));
                banModel.setUid(Integer.valueOf(userInfoModel.getPid()));
                banModel.setOp_code(actionId);
                jsonStr = JSONObject.toJSONString(banModel);
                logger.info("ban json:"+jsonStr);
            }else if(3==actionId||4==actionId){

                if (3==actionId){
                    sendUrl=sendUrl+"/mail";
                }else {
                    sendUrl=sendUrl+"/alter_player_info";
                }

                logger.info("sendurl "+sendUrl );
                UserUpdateModel userUpdateModel = new UserUpdateModel();
                userUpdateModel.setPid(Integer.valueOf(userInfoModel.getPid()));
                userUpdateModel.setServer_id(Integer.valueOf(userInfoModel.getServerId()));
                userUpdateModel.setCoins(userInfoModel.getCoins());
                userUpdateModel.setDiamond(userInfoModel.getDiamond());
                userUpdateModel.setCoupon(userInfoModel.getCoupon());

                userUpdateModel.setTicket(userInfoModel.getTicket());

                jsonStr = JSONObject.toJSONString(userUpdateModel);
                logger.info("update json:"+jsonStr);
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
                ServerRes serverRes = JSONObject.parseObject(res, ServerRes.class);
                logger.info("game res:"+serverRes);
                if (serverRes.getCode()==0){
                    responseModel.setCode(successCode);
                }else {
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("call game server get error code:"+serverRes.getCode());
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
            if (responseModel.getCode()!=errorCode){
                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            }
            }



        return responseModel;
    }

    private UserRes getUserRes(ServerStatusDto dto,String uid){
        UserRes userRes = null;
        String sendUrl = HTTP_PREFIX+dto.getUrl()+"/get_player_info";
        logger.info("sendurl:"+sendUrl);
        HttpPost post = new HttpPost(sendUrl);
        UserSearchModel searchModel = new UserSearchModel();
        searchModel.setServer_id(Integer.valueOf(dto.getServer_id()));

        searchModel.setUid(Integer.valueOf(uid));
        String jsonStr = JSONObject.toJSONString(searchModel);
        logger.info("send json:"+jsonStr);
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
            logger.info(" game res:"+res);
            if(res!=null){
                 userRes =JSONObject.parseObject(res, UserRes.class);
                return userRes;
            }

        }catch (Throwable t){
            logger.error("search user error");

        }finally {
            try {
                response.close();
                EntityUtils.consume(entity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    return userRes;
    }

}

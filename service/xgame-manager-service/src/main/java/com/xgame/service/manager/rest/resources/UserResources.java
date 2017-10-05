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
        String user = getUid();
        String op = "[UserResources] user "+user+" search uid "+uid+" serverid "+ serverId;
        operationLog(uid,op);
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
            List<ServerStatusDto> dtos = statusService.getAllActive();

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
                responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist or active");
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
                if(res!=null){
                    UserRes userRes =JSONObject.parseObject(res, UserRes.class);
                    responseModel.setData(userRes);
                    responseModel.setCode(successCode);
                }

            }catch (Throwable t){

                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
                logger.error("[UserResources] search user error"+ExceptionUtils.getMessage(t));
            }finally {
                try {

                    if (response!=null){
                        response.close();
                    }
                    EntityUtils.consume(entity);

                } catch (IOException e) {
                    logger.error("[UserResources] search user error"+ExceptionUtils.getMessage(e));
                   responseModel.setCode(errorCode);
                   responseModel.setMessage(ExceptionUtils.getMessage(e));

                }
            }
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            logger.error("[UserResources] search user error"+ExceptionUtils.getMessage(t));
        }


        return responseModel;
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateUser(List<UserInfoModel> userInfoModels){
        String uid = getUid();
        String op = "[UserResources] update user";
        UserInfoModel userInfoModel = userInfoModels.get(0);
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            int actionId = userInfoModel.getActionId();
            String serverId = userInfoModel.getServerId();

            List<ServerStatusDto> dtos = statusService.getAllActive();
            ServerStatusDto dto = getDtoById(serverId, dtos);
            if (dto==null){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist or active");
                return responseModel;
            }
            String sendUrl = HTTP_PREFIX+dto.getUrl();

            String jsonStr =null;
            //封号 解封
            if (1==actionId||2==actionId){
                sendUrl=sendUrl+"/ban";
                logger.info("[UserResources]sendurl "+sendUrl);
                UserBanModel banModel = new UserBanModel();
                banModel.setServer_id(Integer.valueOf(userInfoModel.getServerId()));
                banModel.setUid(Integer.valueOf(userInfoModel.getPid()));
                banModel.setOp_code(actionId);
                jsonStr = JSONObject.toJSONString(banModel);
                op = op+" ban user .uid="+userInfoModel.getPid()+" serverid="+serverId+" opcode="+actionId;
            }else if(3==actionId||4==actionId){

                if (3==actionId){
                    sendUrl=sendUrl+"/mail";
                    logger.info("[UserResources]sendurl "+sendUrl);
                    op = op + " send mail";
                }else {
                    sendUrl=sendUrl+"/alter_player_info";
                    logger.info("[UserResources]sendurl "+sendUrl);
                    op = op + " update user property";
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
                op=op+" update json "+jsonStr;
                logger.info("[UserResources]update json "+jsonStr);
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
                    operationLog(uid,op);
                }else {
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("call game server get error code:"+serverRes.getCode());

                }

            }catch (Throwable t){

                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
                logger.error("[UserResources] update user error"+ExceptionUtils.getMessage(t));
            }finally {
                try {

                    if(response!=null){
                        response.close();
                    }
                    EntityUtils.consume(entity);

                } catch (IOException e) {
                    responseModel.setCode(errorCode);
                    responseModel.setMessage(ExceptionUtils.getStackTrace(e));
                    logger.error("[UserResources] update user error"+ExceptionUtils.getMessage(e));
                }
            }
        }catch (Throwable t){
            if (responseModel.getCode()!=errorCode){
                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
               }
            logger.error("[UserResources] update user error"+ExceptionUtils.getMessage(t));
            }



        return responseModel;
    }

    private UserRes getUserRes(ServerStatusDto dto,String uid){
        UserRes userRes = null;
        String sendUrl = HTTP_PREFIX+dto.getUrl()+"/get_player_info";
        logger.info("[UserResources]sendurl:"+sendUrl);
        HttpPost post = new HttpPost(sendUrl);
        UserSearchModel searchModel = new UserSearchModel();
        searchModel.setServer_id(Integer.valueOf(dto.getServer_id()));

        searchModel.setUid(Integer.valueOf(uid));
        String jsonStr = JSONObject.toJSONString(searchModel);
        logger.info("[UserResources]send json:"+jsonStr);
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
            logger.info("[UserResources] game res:"+res);
            if(res!=null){
                 userRes =JSONObject.parseObject(res, UserRes.class);
                return userRes;
            }

        }catch (Throwable t){
            logger.error("[UserResources]search user error"+ExceptionUtils.getMessage(t));

        }finally {
            try {
                if(response!=null){
                    response.close();
                }
                EntityUtils.consume(entity);

            } catch (IOException e) {
                logger.error("[UserResources] get userres error"+e);
            }
        }

    return userRes;
    }

}

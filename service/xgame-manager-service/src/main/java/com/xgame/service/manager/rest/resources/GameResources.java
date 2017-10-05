package com.xgame.service.manager.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.game.GameSettingResModel;
import com.xgame.service.manager.rest.model.game.GameSignCostModel;
import com.xgame.service.manager.rest.model.game.GameWinnerRewardsModel;
import com.xgame.service.manager.rest.model.response.GameSettingList;
import com.xgame.service.manager.rest.model.response.ServerRes;
import com.xgame.service.manager.rest.model.server.ServerBoxModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Path("/gameSetting")
public class GameResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(GameResources.class.getName());
    @GET
    @Path("/getServerBox")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getServerBox(){
        String uid = getUid();
        String op = "[GameResources] "+uid+" getserver box";
        operationLog(uid,op);
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            List<ServerStatusDto> dtos = statusService.getAll();
            List<ServerBoxModel> boxModels = parseServerStatusDto2BoxModel(dtos);
            responseModel.setData(boxModels);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            logger.error("[GameResources] get server box error "+ExceptionUtils.getMessage(t));
        }

        return responseModel;
    }

    @GET
    @Path("/getGameSetting")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getGameSetting(@QueryParam("serverId") String serverId){
        String uid =getUid();
        String op = "[GameResources] "+uid+" get server " + serverId + " setting";
        operationLog(uid,op);

        WrapResponseModel responseModel = new WrapResponseModel();

        try {
            List<ServerStatusDto> dtos = statusService.getAllActive();
            ServerStatusDto dto = getDtoById(serverId, dtos);
            if (dto==null){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist or active");
                return responseModel;
            }
            String sendUrl = HTTP_PREFIX+dto.getUrl()+"/match_config_list";

            HttpGet get = new HttpGet(sendUrl);

            CloseableHttpResponse response =null;
            HttpEntity entity=null;
            try {
                response = httpclient.execute(get);
                entity = response.getEntity();
                String res = EntityUtils.toString(entity, "UTF-8");
                if(null==res){
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("call game server get null");
                }
                GameSettingList gameSettingList = JSONObject.parseObject(res, GameSettingList.class);
                responseModel.setData(gameSettingList.getList());

            }catch (Throwable t){

                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
                logger.error("[GameResources] getGameSetting error "+ExceptionUtils.getMessage(t));
            }finally {
                try {
                    if (response!=null){
                        response.close();
                    }
                    EntityUtils.consume(entity);

                } catch (IOException e) {
                    responseModel.setMessage(ExceptionUtils.getMessage(e));
                    logger.error("[GameResources] getGameSetting error "+ExceptionUtils.getMessage(e));
                }
            }
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[GameResources] getGameSetting error "+ExceptionUtils.getMessage(t));
        }


        responseModel.setCode(successCode);
        return responseModel;
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel update(GameSettingResModel model){
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            String uid = getUid();
            String serverId = String.valueOf(model.getServer_id());
            List<ServerStatusDto> serverStatusDtos =statusService.getAllActive();
            ServerStatusDto dto = getDtoById(serverId, serverStatusDtos);
            if (null==dto){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist or active");
                return responseModel;
            }
            String sendUrl = HTTP_PREFIX+dto.getUrl()+"/match_config_update";

            String jsonStr = JSONObject.toJSONString(model);

            String op = "[GameResources] update server "+model.getServer_id()+" setting:"+jsonStr;

            HttpPost post = new HttpPost(sendUrl);
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
                ServerRes serverRes = JSONObject.parseObject(res, ServerRes.class);
                if (serverRes.getCode()==successCode){
                    operationLog(uid,op);
                    responseModel.setCode(successCode);
                }else {
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("call game server get error code");
                }

            }catch (Throwable t){

                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
                logger.error("[GameResources] update GameSetting error "+ExceptionUtils.getMessage(t));
            }finally {
                try {
                    if (response!=null){
                        response.close();
                    }

                    EntityUtils.consume(entity);

                } catch (IOException e) {

                    responseModel.setMessage(ExceptionUtils.getMessage(e));
                    logger.error("[GameResources] updateGameSetting error "+ExceptionUtils.getMessage(e));
                }
            }
        }catch (Throwable t){

            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            logger.error("[GameResources] update GameSetting error "+ExceptionUtils.getMessage(t));
        }

        return  responseModel;

    }


    private String parse2WinnerRewardStr(List<GameWinnerRewardsModel> models){
        StringBuilder sb = new StringBuilder();
        for (GameWinnerRewardsModel model:models){
            sb.append(model.getGtype()).append("|").append(model.getGid())
                    .append("|").append(model.getCount()).append("|").append(model.getWeight())
                    .append(";");
        }
        return sb.toString();
    }

    private String parse2SignCostStr( List<GameSignCostModel> models){
        StringBuilder sb = new StringBuilder();
        for(GameSignCostModel model:models){
            sb.append(model.getGtype()).append("|").append(model.getGid()).append("|")
                    .append(model.getCount()).append(";");

        }
        return sb.toString();
    }

}

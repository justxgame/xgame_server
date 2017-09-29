package com.xgame.service.manager.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.ServiceConfiguration;
import com.xgame.service.manager.db.dao.ServerStatusDao;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.response.ServerRes;
import com.xgame.service.manager.rest.model.server.ServerBoxModel;
import com.xgame.service.manager.rest.model.server.ServerInfoModel;
import com.xgame.service.manager.rest.model.server.ServerUpdateModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Path("/serverSetting")
public class ServerResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(ServerResources.class.getName());
    @GET
    @Path("/getServerBox")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getServerBox(){
        String uid = getUid();
        String op = "[ServerResources] user "+uid +" get  serverbox";
        operationLog(uid,op);

        WrapResponseModel responseModel = new WrapResponseModel();
        ServerBoxModel boxModel1 = new ServerBoxModel();
        boxModel1.setServerId("ALL");
        boxModel1.setServerName("全服");

        try {
            List<ServerStatusDto> dtos = statusService.getAll();
            List<ServerBoxModel> boxModels = parseServerStatusDto2BoxModel(dtos);
            boxModels.add(boxModel1);
            responseModel.setData(boxModels);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }

        return responseModel;
    }


    @GET
    @Path("/getServerInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getServerInfo(@QueryParam("serverId")String serverId){
        String uid = getUid();
        String op = "[ServerResources] user "+uid +" get  server "+ serverId+" info";
        operationLog(uid,op);
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            List<ServerStatusDto> dtos = statusService.getAll();
            List<ServerInfoModel> models = parseServerStatusDto2ServerInfoModel(dtos);
            if ("all".equalsIgnoreCase(serverId)){
                responseModel.setData(models);
                responseModel.setCode(successCode);
            }else {
                List<ServerInfoModel> speficModel = getServerInfoModelsById(serverId, models);
                responseModel.setData(speficModel);
                responseModel.setCode(successCode);
            }

        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));

        }
        return responseModel;
    }



    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateServer(ServerInfoModel serverInfoModel){

        String uid = getUid();
        String op = "[ServerResources] update server";
        int actionId = serverInfoModel.getActionId();
        WrapResponseModel responseModel = new WrapResponseModel();
        ServerStatusDto statusDto = parseServerInfoModel2StatusDto(serverInfoModel);

        //新增
        if (actionId==1){
            try {

                statusService.saveDto(statusDto);
                op= op+" add new server "+statusDto;
                operationLog(uid,op);
                responseModel.setCode(successCode);
            }catch (Throwable t){
                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            }
            return responseModel;
        }else if(actionId==2){
            //修改
            try {
                statusService.updateDto(statusDto);
                op="op"+ "update server "+statusDto;
                operationLog(uid,op);
                responseModel.setCode(successCode);
            }catch (Throwable t){
                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            }
            return responseModel;

        }else if(actionId==3){

            //删除
            try {
                op="op"+ "delete server "+statusDto;
                operationLog(uid,op);
                statusService.deleteById(statusDto.getServer_id());

                responseModel.setCode(successCode);


            }catch (Throwable t){
                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            }
            return responseModel;

        }
        List<ServerStatusDto> dtos = statusService.getAll();
        String serverId = serverInfoModel.getServerId();


        //全服操作
        if ("all".equalsIgnoreCase(serverInfoModel.getServerId())) {
            op = op+" update all server";
            try {
                for (ServerStatusDto dto:dtos){

                    updateServer(dto,httpclient,actionId,serverId);
                }
                op=" update all server success";
                responseModel.setCode(successCode);
                operationLog(uid,op);
            }catch (Throwable t){
                responseModel.setCode(errorCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            }


        }else {
            ServerStatusDto dto = getDtoById(serverId,dtos);
            if (null==dto){
                responseModel.setCode(errorCode);
                responseModel.setMessage("Server "+serverId+" not exist.can't update");
                return responseModel;
            }try {
                updateServer(dto,httpclient,actionId,serverId);
                responseModel.setCode(successCode);
            }catch (Throwable t){
                responseModel.setCode(successCode);
                responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            }

        }

        return responseModel;
    }


    private void updateServer(ServerStatusDto dto,CloseableHttpClient httpclient,int actionId,String serverId) throws IOException {
        ServerUpdateModel updateModel = new ServerUpdateModel();
        String jsonStr=null;
        String sendUrl = HTTP_PREFIX+dto.getUrl();
        updateModel.setService("all");
        jsonStr= JSONObject.toJSONString(updateModel);

        String uid = getUid();
        String op ="[ServerResources] update server";
        HttpPost post =null;
        if (4==actionId){
            logger.info("start server "+dto.getServer_id());
            sendUrl = sendUrl+"/server_start";
            post = new HttpPost(sendUrl);
            op = op+" start server "+dto.getServer_id();

        }else if(5==actionId){
            logger.info("stop server "+dto.getServer_id());
            sendUrl = sendUrl+"/server_stop";
            post = new HttpPost(sendUrl);
            op = op+" stop server "+dto.getServer_id();
        }else if(6==actionId){
            logger.info("restart server "+dto.getServer_id());
            sendUrl = sendUrl+"/server_restart";
            post = new HttpPost(sendUrl);
            op = op+" restart server "+dto.getServer_id();
        }
        StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
        reqEntity.setContentEncoding("UTF-8");
        reqEntity.setContentType("application/json");

        post.setEntity(reqEntity);
        CloseableHttpResponse response =null;
        HttpEntity entity=null;
        try {
            response = httpclient.execute(post);
            entity = response.getEntity();
            String restr = EntityUtils.toString(entity, "UTF-8");
            ServerRes res = JSONObject.parseObject(restr,ServerRes.class);
            if (res.getCode() == 1) {
                logger.error("update server get error");
            }
            operationLog(uid,op);

        }finally {
            try {
                if (response!=null){
                    response.close();
                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                logger.error("update server error"+e);
            }
        }
    }






}

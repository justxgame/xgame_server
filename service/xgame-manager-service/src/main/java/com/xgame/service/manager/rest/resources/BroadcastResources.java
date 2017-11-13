package com.xgame.service.manager.rest.resources;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.SMS;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.common.type.TimeType;
import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.manager.ServiceConfiguration;
import com.xgame.service.manager.db.dto.BroadCastDto;
import com.xgame.service.manager.db.dto.BroadCastRegularDto;
import com.xgame.service.manager.db.dto.PushDto;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.broadcast.BroadCastModel;
import com.xgame.service.manager.rest.model.broadcast.BroadCastRegularBoxModel;
import com.xgame.service.manager.rest.model.broadcast.BroadCastRegularModel;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Path("/broadcast")
public class BroadcastResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(BroadcastResources.class.getName());
    private static final Integer BROADCAST =1;
    private static final Integer PUSH =2;
    private static String jpushKey = ServiceConfiguration.getInstance().getConfig().getString("xgame.jpush.appkey");
    private static String jpushSecret = ServiceConfiguration.getInstance().getConfig().getString("xgame.jpush.secret");
    protected static JPushClient pushClient;
    static {
        ClientConfig config = ClientConfig.getInstance();
        config.setMaxRetryTimes(3);
        config.setConnectionTimeout(10 * 1000); // 10 seconds
        config.setSSLVersion("TLSv1.1");
        pushClient = new JPushClient(jpushSecret, jpushKey, null, config);
    }
    /**
     * 发送新广播和消息
     * @param broadCastModel
     * @return
     */
    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel sendBroadCast(BroadCastModel broadCastModel){
        logger.info("[BroadcastResources]send:");
        WrapResponseModel responseModel = new WrapResponseModel();
        String uid = getUid();
        String op = "[BroadcastResources] send msg[" + broadCastModel.getMessage() + "] to server " +
                broadCastModel.getServerId()+" type :"+broadCastModel.getType();

        try {
            Integer type = broadCastModel.getType();
            //检查类型
            if (isIllegalBroadCastType(type)){
                responseModel.setCode(errorCode);
                responseModel.setMsg("[BroadcastResources] type "+type +" is illegal type");
                return responseModel;
            }

            String token = requestContext.getHeaderString("token");
            String userName = tokenService.getUserNameByToken(token);
            BroadCastDto broadCastDto = parseBroadcastMode2Dto(broadCastModel,userName);
            if (PUSH==type){

                //推送消息
                sendPush(broadCastDto.getMsg(),broadCastDto.getIndate());
                operationLog(uid,op);
                broadcastService.saveObject(broadCastDto);
                responseModel.setCode(successCode);
                return responseModel;
            }
            List<ServerStatusDto> dtos = statusService.getAllActive();


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
            logger.error("[BroadcastResources] send msg error "+ExceptionUtils.getMessage(t));
        }

        return responseModel;
    }


    /**
     * 获取所有广播
     * @param serverId
     * @return
     */
    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getHistory(@QueryParam("serverId")String serverId,@QueryParam("type")Integer type){
        logger.info("[BroadcastResources] get server " +serverId +"type "+ type+" broadcast");
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            if (isIllegalBroadCastType(type)){
                responseModel.setCode(errorCode);
                responseModel.setMsg("[BroadcastResources] type "+type +" is illegal type");
                return responseModel;
            }
            List<BroadCastDto> broadCastDtos = broadcastService. getAllBroadCast(type);
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
            logger.error("[BroadcastResources] get history broadcast error "+ExceptionUtils.getMessage(t));
        }


        return responseModel;
    }

    /**
     * 删除广播
     * @param model
     * @return
     */
    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(BroadCastModel model){
        WrapResponseModel responseModel = new WrapResponseModel();
        String uid = getUid();
        String op = "[BroadcastResources] delete msg .serverid:" + model.getServerId() + " msg:" + model.getMessage();
        operationLog(uid,op);
        try {
            broadcastService.deleteMsg(model.getTransection());

            responseModel.setCode(successCode);

        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
            logger.error("[BroadcastResources] delete msg error "+ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }



    /**
     * 获取所有定时 task
     * @return
     */
    @GET
    @Path("/regular/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getBroadCastRegularTasks(@QueryParam("type")Integer type){
        WrapResponseModel responseModel = new WrapResponseModel();
        String uid = getUid();
        String op = "[BroadcastResources] get regular/tasks by type "+type;
        operationLog(uid,op);
        try {
            if (isIllegalBroadCastType(type)){
                responseModel.setCode(errorCode);
                responseModel.setMsg("[BroadcastResources] type "+type +" is illegal type");
                return responseModel;
            }
            List<BroadCastRegularDto> dtos = broadcastService.getRegularTaskByType(type);
            List<BroadCastRegularModel> models = new ArrayList<>();
            for (BroadCastRegularDto dto:dtos){
                BroadCastRegularModel model = parseRegularDto2Model(dto);
                models.add(model);
            }
            responseModel.setData(models);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[BroadcastResources] /regular/tasks get error "+ExceptionUtils.getMessage(t));
        }

        return responseModel;
    }

    /**
     * 创建新的 task
     * @param model
     * @return
     */
    @POST
    @Path("/regular/new")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel addNewRegularTask(BroadCastRegularModel model){
        WrapResponseModel responseModel = new WrapResponseModel();

        try {
            String uid = getUid();
            String op = "[BroadcastResources] add new regular task. serverId="+model.getServerId()
                    +" freq_unid="+model.getFreqUnit()+" freq_val="+model.getFreqVal()+" msg="+model.getMsg()+" type="+model.getType();
            operationLog(uid,op);
            if (isIllegalBroadCastType(model.getType())){
                responseModel.setCode(errorCode);
                responseModel.setMsg("[BroadcastResources] type "+model.getType() +" is illegal type");
                return responseModel;
            }
            Long now = System.currentTimeMillis();
            model.setIndate(CommonUtil.getDsFromUnixTimestamp(now));
            Long nextStartDate = calculateNextDate(model.getFreqUnit(), model.getFreqVal(), now);
            model.setNextSendDate(CommonUtil.getDsFromUnixTimestamp(nextStartDate));
            model.setUid(uid);
            BroadCastRegularDto dto = parseRegulartModel2Dto(model);
            broadcastService.saveRegularTask(dto);
            responseModel.setCode(successCode);

        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[BroadcastResources] /regular/tasks get error "+ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }

    /**
     * 根据 transection 删除 task
     * @param model
     * @return
     */
    @POST
    @Path("/regular/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel deleteRegularTask(BroadCastRegularModel model){
        WrapResponseModel responseModel = new WrapResponseModel();
        Long transectionId= model.getTransection();
        String uid = getUid();
        String op = "[BroadcastResources] regular/delete transection id ="+transectionId;
        operationLog(uid,op);
        try {
            broadcastService.deleteRegularTaskById(transectionId);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[BroadcastResources] /regular/tasks get error "+ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }

    /**
     * 获取定时任务下拉框
     * @return
     */
    @GET
    @Path("/regular/box")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getRegularBox(){
        WrapResponseModel responseModel = new WrapResponseModel();
        String uid = getUid();
        String op = "[BroadcastResources] get regular box";
        operationLog(uid,op);
        try {
            List<BroadCastRegularBoxModel> boxModels = new ArrayList<>();
            BroadCastRegularBoxModel boxModel1 = new BroadCastRegularBoxModel();
            boxModel1.setBoxId("H");
            boxModel1.setBoxName("小时");
            boxModels.add(boxModel1);
            BroadCastRegularBoxModel boxModel2 = new BroadCastRegularBoxModel();
            boxModel2.setBoxId("m");
            boxModel2.setBoxName("分钟");
            boxModels.add(boxModel2);
            BroadCastRegularBoxModel boxModel3 = new BroadCastRegularBoxModel();
            boxModel3.setBoxId("s");
            boxModel3.setBoxName("秒");
            boxModels.add(boxModel3);
            responseModel.setCode(successCode);
            responseModel.setData(boxModels);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[BroadcastResources] /regular/box get error"+ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }




    private BroadCastDto parseBroadcastMode2Dto(BroadCastModel model,String userName){
        BroadCastDto dto = new BroadCastDto();
        dto.setTransection(model.getTransection());
        dto.setIndate(CommonUtil.getFormatDateByNow());
        dto.setMsg(model.getMessage());
        dto.setSend_user(userName);
        dto.setServer_id(model.getServerId());
        dto.setType(model.getType());
        return dto;
    }
    private BroadCastModel parseBroadcastMode2Dto(BroadCastDto dto){
        BroadCastModel model = new BroadCastModel();
        model.setServerId(dto.getServer_id());
        model.setMessage(dto.getMsg());
        model.setSendUserName(dto.getSend_user());
        model.setTransection(dto.getTransection());
        model.setSendDate(CommonUtil.parseStr2Time(dto.getIndate()));
        model.setType(dto.getType());
        return model;
    }

    private BroadCastRegularModel parseRegularDto2Model(BroadCastRegularDto dto){
        BroadCastRegularModel model = new BroadCastRegularModel();
        model.setUid(dto.getUid());
        model.setServerId(dto.getServer_id());
        model.setMsg(dto.getMsg());
        model.setIndate(dto.getIndate());
        model.setFreqUnit(dto.getFreq_unit());
        model.setFreqVal(dto.getFreq_val());
        model.setNextSendDate(dto.getNext_send_date());
        model.setStartDate(dto.getStart_date());
        model.setEndDate(dto.getEnd_date());
        model.setTransection(dto.getTransection());
        model.setType(dto.getType());
        return model;
    }

    private BroadCastRegularDto parseRegulartModel2Dto(BroadCastRegularModel model){
        BroadCastRegularDto dto = new BroadCastRegularDto();
        dto.setUid(model.getUid());
        dto.setServer_id(model.getServerId());
        dto.setFreq_unit(model.getFreqUnit());
        dto.setFreq_val(model.getFreqVal());
        dto.setIndate(model.getIndate());
        dto.setNext_send_date(model.getNextSendDate());
        dto.setMsg(model.getMsg());
        dto.setType(model.getType());
        return dto;
    }

    /**
     * 计算 task 下次开始时间 毫秒级别
     * @param freq_unit
     * @param freq_val
     * @param time
     * @return
     */
    private Long calculateNextDate(String freq_unit,Integer freq_val,Long time){
        TimeType timeType = TimeType.fromString(freq_unit);
        switch (timeType){
            case HOUR: return 60*60*1000*freq_val+time;
            case MINUTE:return 60*1000*freq_val+time;
            case SECOND:return 1*1000*freq_val+time;
            default:return time;
        }
    }

    private void sendPush(String msg,String indate) throws APIConnectionException, APIRequestException {
        PushPayload pushPayload = PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.all())
                .setNotification(Notification.alert(msg)).build();
        PushResult pushResult = pushClient.sendPush(pushPayload);
        if (0!=pushResult.statusCode){
            throw new RuntimeException("[BroadcastResources] call jpush client error.status code="+pushResult.statusCode);
        }

        PushDto pushDto = new PushDto();
        pushDto.setIndate(indate);
        pushDto.setMsg_id(pushResult.msg_id);
        pushDto.setMsg(msg);
        broadcastService.savePush(pushDto);


    }

    private boolean isIllegalBroadCastType(Integer type){
        if (null==type){
            return true;
        }
        if (BROADCAST==type||PUSH==type){
            return false;
        }
        return true;

    }


}

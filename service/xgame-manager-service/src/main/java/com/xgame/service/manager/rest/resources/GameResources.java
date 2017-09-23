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
        logger.info("getServerBox");
        WrapResponseModel responseModel = new WrapResponseModel();
//        List<ServerBoxModel> boxModels = new ArrayList<>();
//        ServerBoxModel boxModel1 = new ServerBoxModel();
//        boxModel1.setServerId("10.1.1.2");
//        boxModel1.setServerName("10.1.1.2");
//        ServerBoxModel boxModel2 = new ServerBoxModel();
//        boxModel2.setServerId("10.1.1.1");
//        boxModel2.setServerName("10.1.1.1");
//        boxModels.add(boxModel1);
//        boxModels.add(boxModel2);
        try {
            List<ServerStatusDto> dtos = statusService.getAll();
            List<ServerBoxModel> boxModels = parseServerStatusDto2BoxModel(dtos);
            responseModel.setData(boxModels);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }



        return responseModel;
    }

    @GET
    @Path("/getGameSetting")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getGameSetting(@QueryParam("serverId") String serverId){
        logger.info("getGameSetting");
        logger.info(serverId);
        WrapResponseModel responseModel = new WrapResponseModel();

        List<ServerStatusDto> dtos = statusService.getAll();
        ServerStatusDto dto = getDtoById(serverId, dtos);
        if (dto==null){
            responseModel.setCode(errorCode);
            responseModel.setMessage("Can't find server by id "+serverId +" please make sure server exist");
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
        }finally {
            try {
                response.close();
                EntityUtils.consume(entity);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        List<GameSettingModel> gameSettingModels = new ArrayList<>();
//        GameSettingModel model1 = new GameSettingModel();
//        model1.setId(1);
//        model1.setGameName("门票三人场");
//        model1.setGmaeType(1);
//        model1.setMatchItemType(1);
//        model1.setMatchMode(1);
//        model1.setMinStartPlayerNum(1);
//        model1.setMaxStartPlayerNum(10);
//        model1.setCanIntMinCoin(1);
//        model1.setCanIntMaxCoin(10);
//        model1.setTableCost(100);
//        model1.setMaxPoint(1000);
//        model1.setInitBase(100);
//        model1.setBaseIncreaseSecond(1);
//        model1.setBaseTimes(60);
//        List<GameSignCostModel> signCostModels = new ArrayList<>();
//        GameSignCostModel signCostModel = new GameSignCostModel();
//        signCostModel.setGid(1);
//        signCostModel.setGtype(1);
//        signCostModel.setCount(2);
//        GameSignCostModel signCostModel2 = new GameSignCostModel();
//        signCostModel2.setGid(1);
//        signCostModel2.setGtype(1);
//        signCostModel2.setCount(2);
//        signCostModels.add(signCostModel);
//        signCostModels.add(signCostModel2);
//        model1.setSignCost(signCostModels);
//        model1.setIconId(1);
//        model1.setEarlyExamMinute(1);
//        model1.setServerId(11);
//        model1.setOpenFlag(1);
//        List<GameWinnerRewardsModel> winnerRewardsModels = new ArrayList<>();
//        GameWinnerRewardsModel winnerRewardsModel = new GameWinnerRewardsModel();
//        winnerRewardsModel.setGid(1);
//        winnerRewardsModel.setCount(1);
//        winnerRewardsModel.setGtype(1);
//        winnerRewardsModel.setWeight(1);
//        winnerRewardsModels.add(winnerRewardsModel);
//        GameWinnerRewardsModel winnerRewardsModel2 = new GameWinnerRewardsModel();
//        winnerRewardsModel2.setGid(1);
//        winnerRewardsModel2.setCount(1);
//        winnerRewardsModel2.setGtype(1);
//        winnerRewardsModel2.setWeight(1);
//        winnerRewardsModels.add(winnerRewardsModel2);
//        model1.setWinnerRewards(winnerRewardsModels);
//        model1.setInitStartScores(1);
//        model1.setRemainPlayerNum(1);
//        model1.setSecondRoundPlayerNumber(2);
//        model1.setPhase2GameRounds(2);
//        model1.setDateWeekDay(2);
//        model1.setDayMonDay(2);
//        model1.setDateDayHour(1);
//        model1.setDateHourMinute(2);
//        model1.setAllowLateMinutes(2);
//        gameSettingModels.add(model1);
//        responseModel.setData(gameSettingModels);


        responseModel.setCode(successCode);
        return responseModel;
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel update(GameSettingResModel model){
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            String serverId = String.valueOf(model.getServer_id());
            List<ServerStatusDto> serverStatusDtos =statusService.getAll();
            ServerStatusDto dto = getDtoById(serverId, serverStatusDtos);
            String sendUrl = HTTP_PREFIX+dto.getUrl()+"/match_config_update";

            String jsonStr = JSONObject.toJSONString(model);
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
                    responseModel.setCode(successCode);
                }else {
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("call game server get error code");
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



        //responseModel.setData();
        return  responseModel;

    }

//    private GameSettingSendModel parse2SendSetting(GameSettingModel model){
//        GameSettingSendModel sendModel = new GameSettingSendModel();
//        List<GameWinnerRewardsModel> winnerRewardsModels = model.getWinnerRewards();
//        String winnerStr = parse2WinnerRewardStr(winnerRewardsModels);
//        List<GameSignCostModel> gameSignCostModels = model.getSignCost();
//        String signCostStr = parse2SignCostStr(gameSignCostModels);
//        sendModel.setSign_cost(signCostStr);
//
//        sendModel.setWinner_rewards(winnerStr);
//        sendModel.setId(model.getId());
//        sendModel.setMatch_iterm_type(model.getMatchItemType());
//        sendModel.setGame_type(model.getGmaeType());
//        sendModel.setName(model.getGameName());
//        sendModel.setMatch_mode(model.getMatchMode());
//        sendModel.setMin_start_player_num(model.getMinStartPlayerNum());
//        sendModel.setMax_start_player_num(model.getMaxStartPlayerNum());
//        sendModel.setCan_int_min_coin(model.getCanIntMinCoin());
//        sendModel.setCan_in_max_coin(model.getCanIntMaxCoin());
//        sendModel.setTable_cost(model.getTableCost());
//        sendModel.setMax_point(model.getMaxPoint());
//        sendModel.setInit_base(model.getInitBase());
//        sendModel.setBase_increase_second(model.getBaseIncreaseSecond());
//        sendModel.setBase_times(model.getBaseTimes());
//        sendModel.setIcon_id(model.getIconId());
//        sendModel.setInit_start_scores(model.getInitStartScores());
//        sendModel.setRemain_player_num(model.getRemainPlayerNum());
//        sendModel.setSecond_round_player_number(model.getSecondRoundPlayerNumber());
//        sendModel.setPhase2_game_rounds(model.getPhase2GameRounds());
//        sendModel.setEarly_exam_minute(model.getEarlyExamMinute());
//        sendModel.setDate_mon_day(model.getDayMonDay());
//        sendModel.setDate_week_day(model.getDateWeekDay());
//        sendModel.setDate_day_hour(model.getDateDayHour());
//        sendModel.setDate_hour_minute(model.getDateHourMinute());
//        sendModel.setAllow_late_minutes(model.getAllowLateMinutes());
//        sendModel.setOpen_flag(model.getOpenFlag());
//        sendModel.setServer_id(model.getServerId());
//        return sendModel;
//    }

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

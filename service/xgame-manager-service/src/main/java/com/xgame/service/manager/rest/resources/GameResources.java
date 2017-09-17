package com.xgame.service.manager.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.rest.model.game.GameSettingModel;
import com.xgame.service.manager.rest.model.game.GameSignCostModel;
import com.xgame.service.manager.rest.model.game.GameWinnerRewardsModel;
import com.xgame.service.manager.rest.model.server.ServerBoxModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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
        List<ServerBoxModel> boxModels = new ArrayList<>();
        ServerBoxModel boxModel1 = new ServerBoxModel();
        boxModel1.setServerId("10.1.1.2");
        boxModel1.setServerName("10.1.1.2");
        ServerBoxModel boxModel2 = new ServerBoxModel();
        boxModel2.setServerId("10.1.1.1");
        boxModel2.setServerName("10.1.1.1");
        boxModels.add(boxModel1);
        boxModels.add(boxModel2);

        responseModel.setData(boxModels);
        responseModel.setCode(successCode);

        return responseModel;
    }

    @GET
    @Path("/getGameSetting")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getGameSetting(@QueryParam("serverId") String serverId){
        logger.info("getGameSetting");
        logger.info(serverId);
        WrapResponseModel responseModel = new WrapResponseModel();
        List<GameSettingModel> gameSettingModels = new ArrayList<>();
        GameSettingModel model1 = new GameSettingModel();
        model1.setId(1);
        model1.setGameName("门票三人场");
        model1.setGmaeType(1);
        model1.setMatchItemType(1);
        model1.setMatchMode(1);
        model1.setMinStartPlayerNum(1);
        model1.setMaxStartPlayerNum(10);
        model1.setCanIntMinCoin(1);
        model1.setCanIntMaxCoin(10);
        model1.setTableCost(100);
        model1.setMaxPoint(1000);
        model1.setInitBase(100);
        model1.setBaseIncreaseSecond(1);
        model1.setBaseTimes(60);
        List<GameSignCostModel> signCostModels = new ArrayList<>();
        GameSignCostModel signCostModel = new GameSignCostModel();
        signCostModel.setGid(1);
        signCostModel.setGtype(1);
        signCostModel.setCount(2);
        GameSignCostModel signCostModel2 = new GameSignCostModel();
        signCostModel2.setGid(1);
        signCostModel2.setGtype(1);
        signCostModel2.setCount(2);
        signCostModels.add(signCostModel);
        signCostModels.add(signCostModel2);
        model1.setSignCost(signCostModels);
        model1.setIconId(1);
        List<GameWinnerRewardsModel> winnerRewardsModels = new ArrayList<>();
        GameWinnerRewardsModel winnerRewardsModel = new GameWinnerRewardsModel();
        winnerRewardsModel.setGid(1);
        winnerRewardsModel.setCount(1);
        winnerRewardsModel.setGtype(1);
        winnerRewardsModel.setWeight(1);
        winnerRewardsModels.add(winnerRewardsModel);
        GameWinnerRewardsModel winnerRewardsModel2 = new GameWinnerRewardsModel();
        winnerRewardsModel2.setGid(1);
        winnerRewardsModel2.setCount(1);
        winnerRewardsModel2.setGtype(1);
        winnerRewardsModel2.setWeight(1);
        winnerRewardsModels.add(winnerRewardsModel2);
        model1.setWinnerRewards(winnerRewardsModels);
        model1.setInitStartScores(1);
        model1.setRemainPlayerNum(1);
        model1.setSecondRoundPlayerNumber(2);
        model1.setPhase2GameRounds(2);
        model1.setDateWeekDay(2);
        model1.setDayMonDay(2);
        model1.setDateDayHour(1);
        model1.setDateHourMinute(2);
        model1.setAllowLateMinutes(2);
        gameSettingModels.add(model1);
        gameSettingModels.add(model1);
        responseModel.setData(gameSettingModels);


        responseModel.setCode(successCode);
        return responseModel;
    }


}

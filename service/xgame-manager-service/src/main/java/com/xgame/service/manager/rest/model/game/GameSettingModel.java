package com.xgame.service.manager.rest.model.game;

import java.util.List;

public class GameSettingModel {
    private Integer id;
    private Integer matchItemType;
    private Integer gmaeType;
    private String gameName;
    private Integer matchMode;
    private Integer minStartPlayerNum;
    private Integer maxStartPlayerNum;
    private Integer canIntMinCoin;
    private Integer canIntMaxCoin;
    private Integer tableCost;
    private Integer maxPoint;
    private Integer initBase;
    private Integer baseIncreaseSecond;
    private Integer baseTimes;
    private List<GameSignCostModel> signCost;
    private Integer iconId;
    private List<GameWinnerRewardsModel> winnerRewards;
    private Integer initStartScores;
    private Integer remainPlayerNum;
    private Integer secondRoundPlayerNumber;
    private Integer phase2GameRounds;
    private Integer earlyExamMinute;
    private Integer dayMonDay;
    private Integer dateWeekDay;
    private Integer dateDayHour;
    private Integer dateHourMinute;
    private Integer allowLateMinutes;
    private Integer openFlag;
    private Integer serverId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMatchItemType() {
        return matchItemType;
    }

    public void setMatchItemType(Integer matchItemType) {
        this.matchItemType = matchItemType;
    }

    public Integer getGmaeType() {
        return gmaeType;
    }

    public void setGmaeType(Integer gmaeType) {
        this.gmaeType = gmaeType;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(Integer matchMode) {
        this.matchMode = matchMode;
    }

    public Integer getMinStartPlayerNum() {
        return minStartPlayerNum;
    }

    public void setMinStartPlayerNum(Integer minStartPlayerNum) {
        this.minStartPlayerNum = minStartPlayerNum;
    }

    public Integer getMaxStartPlayerNum() {
        return maxStartPlayerNum;
    }

    public void setMaxStartPlayerNum(Integer maxStartPlayerNum) {
        this.maxStartPlayerNum = maxStartPlayerNum;
    }

    public Integer getCanIntMinCoin() {
        return canIntMinCoin;
    }

    public void setCanIntMinCoin(Integer canIntMinCoin) {
        this.canIntMinCoin = canIntMinCoin;
    }

    public Integer getCanIntMaxCoin() {
        return canIntMaxCoin;
    }

    public void setCanIntMaxCoin(Integer canIntMaxCoin) {
        this.canIntMaxCoin = canIntMaxCoin;
    }

    public Integer getTableCost() {
        return tableCost;
    }

    public void setTableCost(Integer tableCost) {
        this.tableCost = tableCost;
    }

    public Integer getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(Integer maxPoint) {
        this.maxPoint = maxPoint;
    }

    public Integer getInitBase() {
        return initBase;
    }

    public void setInitBase(Integer initBase) {
        this.initBase = initBase;
    }

    public Integer getBaseIncreaseSecond() {
        return baseIncreaseSecond;
    }

    public void setBaseIncreaseSecond(Integer baseIncreaseSecond) {
        this.baseIncreaseSecond = baseIncreaseSecond;
    }

    public Integer getBaseTimes() {
        return baseTimes;
    }

    public void setBaseTimes(Integer baseTimes) {
        this.baseTimes = baseTimes;
    }

    public List<GameSignCostModel> getSignCost() {
        return signCost;
    }

    public void setSignCost(List<GameSignCostModel> signCost) {
        this.signCost = signCost;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }

    public List<GameWinnerRewardsModel> getWinnerRewards() {
        return winnerRewards;
    }

    public void setWinnerRewards(List<GameWinnerRewardsModel> winnerRewards) {
        this.winnerRewards = winnerRewards;
    }

    public Integer getInitStartScores() {
        return initStartScores;
    }

    public void setInitStartScores(Integer initStartScores) {
        this.initStartScores = initStartScores;
    }

    public Integer getRemainPlayerNum() {
        return remainPlayerNum;
    }

    public void setRemainPlayerNum(Integer remainPlayerNum) {
        this.remainPlayerNum = remainPlayerNum;
    }

    public Integer getSecondRoundPlayerNumber() {
        return secondRoundPlayerNumber;
    }

    public void setSecondRoundPlayerNumber(Integer secondRoundPlayerNumber) {
        this.secondRoundPlayerNumber = secondRoundPlayerNumber;
    }

    public Integer getPhase2GameRounds() {
        return phase2GameRounds;
    }

    public void setPhase2GameRounds(Integer phase2GameRounds) {
        this.phase2GameRounds = phase2GameRounds;
    }

    public Integer getDayMonDay() {
        return dayMonDay;
    }

    public void setDayMonDay(Integer dayMonDay) {
        this.dayMonDay = dayMonDay;
    }

    public Integer getDateWeekDay() {
        return dateWeekDay;
    }

    public void setDateWeekDay(Integer dateWeekDay) {
        this.dateWeekDay = dateWeekDay;
    }

    public Integer getDateDayHour() {
        return dateDayHour;
    }

    public void setDateDayHour(Integer dateDayHour) {
        this.dateDayHour = dateDayHour;
    }

    public Integer getDateHourMinute() {
        return dateHourMinute;
    }

    public void setDateHourMinute(Integer dateHourMinute) {
        this.dateHourMinute = dateHourMinute;
    }

    public Integer getAllowLateMinutes() {
        return allowLateMinutes;
    }

    public void setAllowLateMinutes(Integer allowLateMinutes) {
        this.allowLateMinutes = allowLateMinutes;
    }

    public Integer getEarlyExamMinute() {
        return earlyExamMinute;
    }

    public void setEarlyExamMinute(Integer earlyExamMinute) {
        this.earlyExamMinute = earlyExamMinute;
    }

    public Integer getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Integer openFlag) {
        this.openFlag = openFlag;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
}

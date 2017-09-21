package com.xgame.service.manager.rest.resources;


import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.manager.ServiceContextFactory;
import com.xgame.service.manager.db.dto.RewardBoxDto;
import com.xgame.service.manager.db.dto.RewardOrderDetailDto;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import com.xgame.service.manager.rest.model.reward.RewardItemTypeModel;
import com.xgame.service.manager.rest.model.reward.RewardOrderModel;
import com.xgame.service.manager.rest.model.server.ServerBoxModel;
import com.xgame.service.manager.rest.model.server.ServerInfoModel;
import com.xgame.service.manager.service.BroadcastService;
import com.xgame.service.manager.service.RewardBoxService;
import com.xgame.service.manager.service.RewardOrderDetailService;
import com.xgame.service.manager.service.ServerStatusService;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.ArrayList;
import java.util.List;

public class BaseResources {
    protected static final String HTTP_PREFIX="http://";
    protected static CloseableHttpClient httpclient;
    {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(5 * 1000)
                .setConnectionRequestTimeout(5 * 1000)
                .setSocketTimeout(5 * 1000).build();
        httpclient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    }

//    protected AdxConfigManagerService adxConfigManagerService = ServiceContextFactory.adxConfigManagerService;
//    protected AppConfigService appConfigService = ServiceContextFactory.appConfigService;
//    protected ChannelConfigService channelConfigService = ServiceContextFactory.channelConfigService;
//    protected CustomerConfigService customerConfigService = ServiceContextFactory.customerConfigService;
//    protected SystemConfigService systemConfigService = ServiceContextFactory.systemConfigService;
//    protected AdvConfigService advConfigService = ServiceContextFactory.advConfigService;
//    protected AdvStatisticService advStatisticService = ServiceContextFactory.advStatisticService;
//    protected UserService userService = ServiceContextFactory.userService;
//    protected TokenService tokenService = ServiceContextFactory.tokenService;
    protected RewardOrderDetailService rewardOrderDetailService = ServiceContextFactory.rewardOrderDetailService;
    protected RewardBoxService rewardBoxService = ServiceContextFactory.rewardBoxService;
    protected ServerStatusService statusService = ServiceContextFactory.serverStatusService;
    protected BroadcastService broadcastService = ServiceContextFactory.broadcastService;

    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;

    protected String getUid() {
        return requestContext.getHeaderString("uid");
    }

    protected List<RewardOrderModel> parseRewardOrderDetailDto2Model(List<RewardOrderDetailDto> dtos){
        List<RewardOrderModel> models = new ArrayList<>();
        for(RewardOrderDetailDto dto:dtos){
            RewardOrderModel model = new RewardOrderModel();
            model.setOrderId(dto.getOrder_id());
            model.setSupplierOrderId(dto.getOrder_id());
            model.setOrderType(dto.getOrder_status());
            model.setPhoneNo(dto.getPhone_no());
            model.setRewardCount(dto.getItem_count());
            model.setServerId(dto.getServer_id());
            model.setRewardName(dto.getMemo());
            model.setRewardId(dto.getItem_id());
            model.setRewardType(dto.getItem_type());
            model.setAddress(dto.getAddress());
            model.setIndate(CommonUtil.parseStr2Time(dto.getIndate()));
            model.setUid(Integer.valueOf(dto.getUid()));
            model.setException(dto.getOrder_exception());
            models.add(model);
        }
        return models;

    }
    protected List<RewardItemTypeModel> parseRewardBoxDto2Model(List<RewardBoxDto> dtos){
        List<RewardItemTypeModel> models = new ArrayList<>();
        for (RewardBoxDto dto:dtos){
            RewardItemTypeModel itemTypeModel = new RewardItemTypeModel();
            itemTypeModel.setItemTypeId(dto.getId());
            itemTypeModel.setItemTypeName(dto.getMemo());
            models.add(itemTypeModel);
        }
        return models;
    }

    protected List<ServerBoxModel> parseServerStatusDto2BoxModel(List<ServerStatusDto> dtos){
        List<ServerBoxModel> boxModels = new ArrayList<>();
        for (ServerStatusDto dto:dtos){
            ServerBoxModel boxModel = new ServerBoxModel();
            boxModel.setServerId(String.valueOf(dto.getServer_id()));
            boxModel.setServerName(dto.getUrl());
            boxModels.add(boxModel);
        }
        return boxModels;
    }

    protected List<ServerInfoModel> parseServerStatusDto2ServerInfoModel(List<ServerStatusDto> dtos){
        List<ServerInfoModel> models = new ArrayList<>();
        for(ServerStatusDto dto:dtos){
            ServerInfoModel model = new ServerInfoModel();
            model.setServerId(String.valueOf(dto.getServer_id()));
            model.setStatus(dto.getStatus());
            model.setIpPort(dto.getUrl());
            model.setServerName(dto.getUrl());
            model.setActionId(0);
            models.add(model);
        }
        return models;
    }
    protected  ServerStatusDto parseServerInfoModel2StatusDto(ServerInfoModel model){
        ServerStatusDto dto = new ServerStatusDto();
        dto.setServer_id(Integer.valueOf(model.getServerId()));
        dto.setGm_port(model.getGmPort());
        String[] ipPort = model.getIpPort().split(":");
        dto.setIp(ipPort[0]);
        dto.setPort(Integer.valueOf(ipPort[1]));
        dto.setUrl(model.getIpPort());
        dto.setStatus(model.getStatus());
        dto.setIndate(CommonUtil.getFormatDateByNow());
        return dto;
    }

    protected ServerStatusDto getDtoById(String serverId, List<ServerStatusDto> dtos){
        for(ServerStatusDto dto:dtos){
            if (dto.getServer_id().equals(Integer.valueOf(serverId))){
                return dto;
            }
        }
        return null;
    }

    protected List<ServerInfoModel> getServerInfoModelsById(String serverId,List<ServerInfoModel> models){
        List<ServerInfoModel> serverInfoModels = new ArrayList<>();
        for (ServerInfoModel model:models){
            if (serverId.equals(model.getServerId())){
                serverInfoModels.add(model);
                return serverInfoModels;
            }
        }
        return serverInfoModels;
    }


}

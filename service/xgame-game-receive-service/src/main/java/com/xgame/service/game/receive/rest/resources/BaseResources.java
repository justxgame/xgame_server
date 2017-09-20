package com.xgame.service.game.receive.rest.resources;

import com.xgame.service.game.receive.ServiceContextFactory;
import com.xgame.service.game.receive.service.RewardOrderService;
import com.xgame.service.game.receive.service.ServerStatusService;
import com.xgame.service.game.receive.service.UserAddressService;
import com.xgame.service.game.receive.service.UserStatusService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

public class BaseResources {

    //    protected AdxConfigManagerService adxConfigManagerService = ServiceContextFactory.adxConfigManagerService;
//    protected AppConfigService appConfigService = ServiceContextFactory.appConfigService;
//    protected ChannelConfigService channelConfigService = ServiceContextFactory.channelConfigService;
//    protected CustomerConfigService customerConfigService = ServiceContextFactory.customerConfigService;
//    protected SystemConfigService systemConfigService = ServiceContextFactory.systemConfigService;
//    protected AdvConfigService advConfigService = ServiceContextFactory.advConfigService;
//    protected AdvStatisticService advStatisticService = ServiceContextFactory.advStatisticService;
//    protected UserService userService = ServiceContextFactory.userService;
//    protected TokenService tokenService = ServiceContextFactory.tokenService;
    protected RewardOrderService rewardOrderService = ServiceContextFactory.rewardOrderService;
    protected ServerStatusService serverStatusService = ServiceContextFactory.serverStatusService;
    protected UserAddressService userAddressService = ServiceContextFactory.userAddressService;
    protected UserStatusService userStatusService = ServiceContextFactory.userStatusService;


    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;

    protected String getUid() {
        return requestContext.getHeaderString("uid");
    }

}

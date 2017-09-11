package com.xgame.service.manager.rest.resources;

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


    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;

    protected String getUid() {
        return requestContext.getHeaderString("uid");
    }

}

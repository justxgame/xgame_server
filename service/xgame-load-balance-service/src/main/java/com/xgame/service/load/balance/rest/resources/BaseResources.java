package com.xgame.service.load.balance.rest.resources;

import com.xgame.service.load.balance.ServiceContextFactory;
import com.xgame.service.load.balance.service.ServerService;
import com.xgame.service.load.balance.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

public class BaseResources {
    private static Logger logger = LoggerFactory.getLogger(BaseResources.class.getName());
    protected UserService userService = ServiceContextFactory.userService;
    protected ServerService serverService = ServiceContextFactory.serverService;

    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;
}

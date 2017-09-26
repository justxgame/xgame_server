package com.xgame.order.consumer.rest.resource;


import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

public class BaseResources {

    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;

}

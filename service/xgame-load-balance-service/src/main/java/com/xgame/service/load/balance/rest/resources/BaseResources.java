package com.xgame.service.load.balance.rest.resources;

import com.xgame.service.load.balance.ServiceConfiguration;
import com.xgame.service.load.balance.ServiceContextFactory;
import com.xgame.service.load.balance.service.ServerService;
import com.xgame.service.load.balance.service.UserLoginService;
import com.xgame.service.load.balance.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import java.util.HashMap;
import java.util.Map;

public class BaseResources {
    private static Logger logger = LoggerFactory.getLogger(BaseResources.class.getName());
    protected UserService userService = ServiceContextFactory.userService;
    protected ServerService serverService = ServiceContextFactory.serverService;
    protected UserLoginService userLoginService = ServiceContextFactory.userLoginService;
    private static Map<String,String> ipHostMap = new HashMap<>();
    static {
        String ipHost = ServiceConfiguration.getInstance().getConfig().getString("xgame.ip.host.mapping");
        String[] ipHostArr = ipHost.split(",");
        for (String iphost :ipHostArr){
            String[] iparr = iphost.split(":");
            ipHostMap.put(iparr[0],iparr[1]);
        }
    }
    @Inject
    ContainerRequestContext requestContext;

    protected final int successCode = 0;
    protected final int errorCode = -1;
    protected final int maxOnlineCode = -10;
    protected final Integer onlineFlag =1;
    protected final Integer offlineFlag = 0;

    protected String getIpHost(String ip){
        if (null==ip||ip.isEmpty()){
            return null;
        }
        return ipHostMap.get(ip);
    }
}

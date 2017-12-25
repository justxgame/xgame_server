package com.xgame.service.load.balance.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.load.balance.ServiceConfiguration;
import com.xgame.service.load.balance.rest.model.AppCheck;
import com.xgame.service.load.balance.rest.model.UserInfo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/app")
public class AppCheckResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(AppCheckResources.class.getName());

    @GET
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel appleCheck(UserInfo userInfo) {
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            int appleCheckFlag = ServiceConfiguration.getInstance().getConfig().getInt("xgame.applecheck.flag");
            AppCheck appCheck = new AppCheck();
            appCheck.setCheckFlag(appleCheckFlag);
            responseModel.setData(appCheck);
            responseModel.setCode(successCode);
        } catch (Throwable t) {
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            responseModel.setMsg("服务器连接错误，请稍后重试");
            logger.error("[AppCheckResources] appleCheck failed " + ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }
}

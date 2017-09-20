package com.xgame.service.game.receive.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.game.receive.db.dto.UserStatusDto;
import com.xgame.service.game.receive.rest.model.user.UserAddressReportModel;
import com.xgame.service.game.receive.rest.model.user.UserStatusReportModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(UserResources.class.getName());
    @POST
    @Path("/address/report")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel userAddressReport(String str){
        logger.info("user address report");
        logger.info("model:"+str);
        WrapResponseModel responseModel = new WrapResponseModel();
        UserAddressReportModel model = JSONObject.parseObject(str, UserAddressReportModel.class);
        try {
            userAddressService.saveObject(model);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }



        return responseModel;
    }

    @POST
    @Path("/status/report")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel userStatusReport(String str){
        logger.info("user status report");
        logger.info("model:"+str);
        WrapResponseModel responseModel = new WrapResponseModel();
        UserStatusReportModel model = JSONObject.parseObject(str, UserStatusReportModel.class);
        UserStatusDto dto = parseUserStatusMode2Dto(model);
        try {
            userStatusService.saveObjet(dto);
            responseModel.setCode(successCode);
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }



        return responseModel;
    }

    private UserStatusDto parseUserStatusMode2Dto(UserStatusReportModel model){
        UserStatusDto dto = new UserStatusDto();
        dto.setUid(model.getUid());
        dto.setServer_id(model.getServer_id());
        dto.setOnline_flag(CommonUtil.parseBoolean2Int(model.getOnline_flag()));
        dto.setNick_name(model.getNick_name());
        dto.setIndate(CommonUtil.getFormatDateByNow());
        return dto;
    }

}

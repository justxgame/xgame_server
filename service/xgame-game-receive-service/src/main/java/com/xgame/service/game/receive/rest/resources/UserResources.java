package com.xgame.service.game.receive.rest.resources;

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
    public WrapResponseModel userAddressReport(UserAddressReportModel model) {
        logger.info("[user:userAddressReport] model=" + model);
        WrapResponseModel responseModel = new WrapResponseModel();
        try {
            logger.info(model.toString());
            userAddressService.saveObject(model);
            responseModel.setCode(successCode);
        } catch (Throwable t) {
            logger.error("[user:userAddressReport] failed", t);
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }
        return responseModel;
    }

    @POST
    @Path("/status/report")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel userStatusReport(UserStatusReportModel model) {
        logger.info("[user:userStatusReport] model=" + model);
        WrapResponseModel responseModel = new WrapResponseModel();
        UserStatusDto dto = parseUserStatusMode2Dto(model);
        try {
            userStatusService.saveObjet(dto);
            responseModel.setCode(successCode);
        } catch (Throwable t) {
            logger.error("[user:userStatusReport] failed ", t);
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }
        return responseModel;
    }

    private UserStatusDto parseUserStatusMode2Dto(UserStatusReportModel model) {
        UserStatusDto dto = new UserStatusDto();
        dto.setUid(model.getUid());
        dto.setServer_id(model.getServer_id());
        dto.setOnline_flag(CommonUtil.parseBoolean2Int(model.getOnline_flag()));
        dto.setNick_name(model.getNick_name());
        dto.setIndate(CommonUtil.getFormatDateByNow());
        return dto;
    }

}

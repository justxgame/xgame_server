package com.xgame.service.game.receive.rest.resources;

import com.alibaba.fastjson.JSONObject;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.game.receive.db.dto.ServerStatusDto;
import com.xgame.service.game.receive.rest.model.server.ServerStatusReportModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/server")
public class ServerStatusResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(ServerStatusResources.class.getName());

    @POST
    @Path("/status/report")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel serverReport(ServerStatusReportModel model) {
        logger.info("[server:serverReport] mode=" + model);
        WrapResponseModel responseModel = new WrapResponseModel();
        ServerStatusDto dto = parseServerStatusModel2Dto(model);
        try {
            serverStatusService.saveObject(dto);
            responseModel.setCode(successCode);
        } catch (Throwable t) {
            logger.error("[server:serverReport] failed", t);
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getStackTrace(t));
        }
        return responseModel;
    }

    private ServerStatusDto parseServerStatusModel2Dto(ServerStatusReportModel model) {
        ServerStatusDto dto = new ServerStatusDto();
        dto.setGm_port(model.getGm_port());
        dto.setServer_id(model.getServer_id());
        dto.setIp(model.getIp());
        dto.setPort(model.getPort());
        dto.setStatus(CommonUtil.parseBoolean2Int(model.getStatus()));
        String url = model.getIp() + ":" + model.getGm_port();
        dto.setUrl(url);
        dto.setIndate(CommonUtil.getFormatDateByNow());
        dto.setVersion(model.getVersion());
        dto.setPlatform(model.getPlatform());
        return dto;
    }

}

package com.xgame.service.manager.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.rest.model.server.ServerBoxModel;
import com.xgame.service.manager.rest.model.server.ServerInfoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/serverSetting")
public class ServerResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(ServerResources.class.getName());
    @GET
    @Path("/getServerBox")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getServerBox(){
        logger.info("getServerBox");
        WrapResponseModel responseModel = new WrapResponseModel();
        List<ServerBoxModel> boxModels = new ArrayList<>();
        ServerBoxModel boxModel1 = new ServerBoxModel();
        boxModel1.setServerId("ALL");
        boxModel1.setServerName("全服");
        ServerBoxModel boxModel2 = new ServerBoxModel();
        boxModel2.setServerId("10.1.1.1");
        boxModel2.setServerName("10.1.1.1");
        boxModels.add(boxModel1);
        boxModels.add(boxModel2);

        responseModel.setData(boxModels);
        responseModel.setCode(successCode);

        return responseModel;
    }


    @GET
    @Path("/getServerInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getServerInfo(@QueryParam("serverId")String serverId){
        WrapResponseModel responseModel = new WrapResponseModel();
        List<ServerInfoModel> serverInfoModels = new ArrayList<>();
        ServerInfoModel serverInfoModel = new ServerInfoModel();
        serverInfoModel.setServerName("服务器1");
        serverInfoModel.setIpPort("10.1.1.1:8111");
        serverInfoModel.setStatus(1);
        serverInfoModels.add(serverInfoModel);
        ServerInfoModel serverInfoModel1 = new ServerInfoModel();
        serverInfoModel1.setServerName("服务器1");
        serverInfoModel1.setIpPort("10.1.1.1:8111");
        serverInfoModel1.setStatus(1);
        serverInfoModels.add(serverInfoModel1);
        responseModel.setData(serverInfoModels);
        responseModel.setCode(successCode);

        return responseModel;
    }



    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateServer(ServerInfoModel serverInfoModel){
        WrapResponseModel responseModel = new WrapResponseModel();
        responseModel.setCode(successCode);
        return responseModel;
    }

}

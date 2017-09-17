package com.xgame.service.manager.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.rest.model.broadcast.BroadCastModel;
import com.xgame.service.manager.rest.model.server.ServerBoxModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/broadcast")
public class BroadcastResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(BroadcastResources.class.getName());

    @POST
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel sendBroadCast(BroadCastModel broadCastModel){
        logger.info("send");
        WrapResponseModel responseModel = new WrapResponseModel();

        responseModel.setCode(successCode);
        return responseModel;
    }

    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getHistory(){
        logger.info("history");
        WrapResponseModel responseModel = new WrapResponseModel();
        List<BroadCastModel> broadCastModels = new ArrayList<>();
        BroadCastModel model1 = new BroadCastModel();
        model1.setMessage("祝贺 xx 获得 xxx");
        model1.setSendDate(System.currentTimeMillis());
        model1.setServerId("10.1.1.1");
        model1.setSendUserName("admin");
        broadCastModels.add(model1);
        BroadCastModel model2 = new BroadCastModel();
        model2.setMessage("祝贺 xx 获得 xxx");
        model2.setSendDate(System.currentTimeMillis());
        model2.setServerId("10.1.1.2");
        model2.setSendUserName("admin2");
        broadCastModels.add(model2);
        responseModel.setData(broadCastModels);

        responseModel.setCode(successCode);
        return responseModel;
    }

    @POST
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel delete(BroadCastModel model){
        WrapResponseModel responseModel = new WrapResponseModel();
        responseModel.setCode(successCode);
        return responseModel;
    }

}

package com.xgame.service.manager.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.rest.model.user.UserInfoModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
public class UserResources extends BaseResources {

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel searchUser(@QueryParam("serverId")String serverId,@QueryParam("uid")String uid,
                                        @QueryParam("userName")String userName){
        WrapResponseModel responseModel = new WrapResponseModel();

        UserInfoModel userInfoModel =new UserInfoModel();
        userInfoModel.setServerId("10.1.1.1");
        userInfoModel.setUserName(userName);
        userInfoModel.setUid("001");
        userInfoModel.setMoney(100);
        userInfoModel.setTicket(1000);
        userInfoModel.setActionId(0);
        responseModel.setData(userInfoModel);
        responseModel.setCode(successCode);
        return responseModel;
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel updateUser(UserInfoModel userInfoModel){
        WrapResponseModel responseModel = new WrapResponseModel();
        responseModel.setCode(successCode);
        return responseModel;
    }
}

package com.xgame.service.load.balance.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.load.balance.ServiceConfiguration;
import com.xgame.service.load.balance.db.dto.ServerDto;
import com.xgame.service.load.balance.db.dto.UserDto;
import com.xgame.service.load.balance.rest.model.ServerInfo;
import com.xgame.service.load.balance.rest.model.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/loadBalance")
public class ServerResources extends BaseResources{
    private static Logger logger = LoggerFactory.getLogger(ServerResources.class.getName());
    @POST
    @Path("/getServerInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getServerInfoByUser(UserInfo userInfo){
        WrapResponseModel responseModel = new WrapResponseModel();

        String userName = userInfo.getUserName();
        if (StringUtils.isEmpty(userName)){
            responseModel.setCode(errorCode);
            responseModel.setMessage("get empty user name");
        }
        Integer serverId = userService.getServerIdByUser(userName);
        ServerInfo serverInfo = new ServerInfo();

        if (null!=serverId){
            ServerDto serverDto = serverService.getServerInfoById(serverId);
            serverInfo.setServerId(serverId);
            serverInfo.setIp(serverDto.getServer_ip());
            serverInfo.setPort(serverDto.getPort());
        }else {
            //获取并计算每个 server当前总在线人数
            List<UserDto> serverOnline = userService.getOnlineUsers();
            List<UserDto> serverOffline = userService.getOfflineUsers();
            for (UserDto userDto:serverOnline){
                for (UserDto offDto:serverOffline){
                    if (userDto.getServer_id()==offDto.getServer_id()){
                        int diff = userDto.getUser_count()-offDto.getUser_count();
                        userDto.setUser_count(diff);
                    }
                }
            }
            //过滤已满载 server
            int maxOnline = ServiceConfiguration.getInstance().getConfig().getInt("xgame.server.max.online");
            List<UserDto> filterUserDto = filterServerByMaxOnline(serverOnline, maxOnline);
            if (filterUserDto.isEmpty()){
                responseModel.setCode(errorCode);
                responseModel.setMessage("All server reached maxOnline");
            }
            //比较 count 获取最多在线 server
            UserDto finalUserDto = filterUserDto.get(0);
            for(UserDto userDto:filterUserDto){
                if (userDto.getUser_count()>finalUserDto.getUser_count()){
                    finalUserDto=userDto;
                }
            }
            serverInfo.setServerId(finalUserDto.getServer_id());
            ServerDto finalServer = serverService.getServerInfoById(finalUserDto.getServer_id());
            serverInfo.setIp(finalServer.getServer_ip());
            serverInfo.setPort(finalServer.getPort());
            responseModel.setCode(successCode);
            responseModel.setData(serverInfo);


        }

        return responseModel;
    }

    public List<UserDto> filterServerByMaxOnline(List<UserDto> userDtos,int maxOnline){
        List<UserDto> result = new ArrayList<>();
        for (UserDto userDto:userDtos){
            if (userDto.getUser_count()<maxOnline){
                result.add(userDto);
            }
        }
        return result;
    }
}

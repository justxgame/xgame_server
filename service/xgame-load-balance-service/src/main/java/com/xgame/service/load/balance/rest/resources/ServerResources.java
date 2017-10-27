package com.xgame.service.load.balance.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.load.balance.ServiceConfiguration;
import com.xgame.service.load.balance.db.dto.ServerDto;
import com.xgame.service.load.balance.db.dto.UserDto;
import com.xgame.service.load.balance.rest.model.ServerInfo;
import com.xgame.service.load.balance.rest.model.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/server")
public class ServerResources extends BaseResources{
    private static Logger logger = LoggerFactory.getLogger(ServerResources.class.getName());
    @POST
    @Path("/getServerInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getServerInfoByUser(UserInfo userInfo){
        WrapResponseModel responseModel = new WrapResponseModel();

        try {
            String userName = userInfo.getUserName();
            logger.info("[ServerResources] get server info by user:"+userName);
            if (StringUtils.isEmpty(userName)){
                responseModel.setCode(errorCode);
                responseModel.setMessage("get empty user name");
                return responseModel;
            }
            Integer serverId = userService.getServerIdByUser(userName);
            ServerInfo serverInfo = new ServerInfo();

            if (null!=serverId){
                ServerDto serverDto = serverService.getServerInfoById(serverId);
                if (null==serverDto){
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("Get serverId "+serverId+" serverInfo is empty, please check server setting");
                    return responseModel;
                }
                serverInfo.setServerId(serverId);
                serverInfo.setIp(serverDto.getIp());
                serverInfo.setPort(serverDto.getPort());
                responseModel.setData(serverInfo);
                responseModel.setCode(successCode);
                return responseModel;
            }else {
                //获取并计算每个 server当前总在线人数
                List<UserDto> serverOnline = getServerOnlineUsers();
                //过滤已满载 server
                int maxOnline = ServiceConfiguration.getInstance().getConfig().getInt("xgame.server.max.online");
                List<UserDto> filterUserDto = filterServerByMaxOnline(serverOnline, maxOnline);
                if (filterUserDto.isEmpty()){
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("All server reached maxOnline");
                    return responseModel;
                }
                //比较 count 获取最多在线 server
                UserDto finalUserDto = getMaxOnlineServer(filterUserDto);

                ServerDto finalServer = serverService.getServerInfoById(finalUserDto.getServer_id());
                if (null == finalServer){
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("Get max online server "+finalUserDto.getServer_id()+" serverInfo is empty, please check server setting");
                    return responseModel;
                }
                serverInfo.setServerId(finalUserDto.getServer_id());
                serverInfo.setIp(finalServer.getIp());
                serverInfo.setPort(finalServer.getPort());
                responseModel.setCode(successCode);
                responseModel.setData(serverInfo);


            }
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[ServerResources] get user login server error "+ExceptionUtils.getMessage(t));
        }


        return responseModel;
    }

    private List<UserDto> filterServerByMaxOnline(List<UserDto> userDtos,int maxOnline){
        List<UserDto> result = new ArrayList<>();
        for (UserDto userDto:userDtos){
            if (userDto.getUser_count()<maxOnline){
                result.add(userDto);
            }
        }
        return result;
    }

    private List<UserDto> getServerOnlineUsers(){
        List<UserDto> serverOnline = userService.getServerUsersByFlag(1);
        List<UserDto> serverOffline = userService.getServerUsersByFlag(0);
        for (UserDto userDto:serverOnline){
            for (UserDto offDto:serverOffline){
                if (userDto.getServer_id().equals(offDto.getServer_id())){
                    int diff = userDto.getUser_count()-offDto.getUser_count();
                    userDto.setUser_count(diff);
                }
            }
        }
        return serverOnline;
    }

    private UserDto getMaxOnlineServer(List<UserDto> userDtos){
        UserDto finalUserDto = userDtos.get(0);
        for(UserDto userDto:userDtos){
            if (userDto.getUser_count()>finalUserDto.getUser_count()){
                finalUserDto=userDto;
            }
        }
        return finalUserDto;
    }

}

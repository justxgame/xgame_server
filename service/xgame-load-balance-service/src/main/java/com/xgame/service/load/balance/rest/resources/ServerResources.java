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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Path("/server")
public class ServerResources extends BaseResources{
    private static Logger logger = LoggerFactory.getLogger(ServerResources.class.getName());

    @POST
    @Path("/getServerInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getServerInfoByUser(UserInfo userInfo){
        WrapResponseModel responseModel = new WrapResponseModel();

        try {
            requireNonNull(userInfo,"[ServerResources] get null userinfo");

            String userName = userInfo.getUserName();
            logger.info("[ServerResources] get server info by user:"+userInfo);
            if (StringUtils.isEmpty(userName)){
                responseModel.setCode(errorCode);
                responseModel.setMessage("[ServerResources] get empty user name");
                responseModel.setMsg("服务器连接错误，请稍后重试");
                return responseModel;
            }
            ServerInfo serverInfo = new ServerInfo();
            if (null==userInfo.getVer()){
                logger.info("[ServerResources] can't get ver.return default ip and port");

                serverInfo.setServer_ip("appstore.zhizhangame.com");
                serverInfo.setServer_port(10018);
                responseModel.setData(serverInfo);
                responseModel.setCode(successCode);
                return responseModel;
            }
            int maxOnline = ServiceConfiguration.getInstance().getConfig().getInt("xgame.server.max.online");
            //查询数据库 判断是否是新用户
            Integer serverId = userService.getServerIdByUser(userName);


            String host=null;

            //非新注册用户
            if (null!=serverId){
                //获取 server 信息
                ServerDto serverDto = serverService.getServerInfoById(serverId);
                //获取不到 server 信息 抛异常
                requireNonNull(serverDto,"[ServerResources] Get serverId "+serverId+" serverInfo is empty, please check server setting and user "+userName+" login status");

                //查询当前 user 是否在线
                Integer userStatus = userService.getUserOnlineStatus(userName);
                if (onlineFlag==userStatus){
                    serverInfo.setServerId(serverId);
                    host = getIpHost(serverDto.getIp());
                    if (null==host||host.isEmpty()){
                        responseModel.setCode(errorCode);
                        responseModel.setMsg("服务器连接错误，请稍后重试");
                        responseModel.setMessage("[ServerResources] get ip "+serverDto.getIp()+" mapping empty,please check setting");
                        return responseModel;
                    }
                    serverInfo.setServer_ip(host);
                    serverInfo.setServer_port(serverDto.getPort());
                    responseModel.setData(serverInfo);
                    responseModel.setCode(successCode);
                    return responseModel;
                }


                //查询服务器是否已满载
                UserDto userDto = userService.getServerCountByIdFlag(serverId,onlineFlag);

                if (userDto==null||(userDto.getUser_count()<maxOnline)){
                    serverInfo.setServerId(serverId);
                    host = getIpHost(serverDto.getIp());
                    if (null==host||host.isEmpty()){
                        responseModel.setCode(errorCode);
                        responseModel.setMsg("服务器连接错误，请稍后重试");
                        responseModel.setMessage("[ServerResources] get ip "+serverDto.getIp()+" mapping empty,please check setting");
                        return responseModel;
                    }
                    serverInfo.setServer_ip(host);
                    serverInfo.setServer_port(serverDto.getPort());
                    responseModel.setData(serverInfo);
                    responseModel.setCode(successCode);
                    return responseModel;
                }else {
                    responseModel.setCode(maxOnlineCode);
                    responseModel.setMessage("[ServerResources] server "+serverId+" reached max online users");
                    responseModel.setMsg("服务器繁忙，请稍后重试");
                    return responseModel;
                }


            }else {

                //获取 active server
                List<ServerDto> activeServer = serverService.getActiveServer(userInfo.getVer(),userInfo.getPlatform());
                if (null==activeServer||activeServer.isEmpty()){
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("[ServerResources] Can't get active server.");
                    responseModel.setMsg("服务器连接错误，请稍后重试");
                    return responseModel;
                }
                //获取并计算每个 server当前总在线人数
                List<UserDto> serverOnline = getServerOnlineUsers(activeServer);
                //过滤已满载 server

                List<UserDto> filterUserDto = filterServerByMaxOnline(serverOnline, maxOnline);
                if (filterUserDto.isEmpty()){
                    responseModel.setCode(maxOnlineCode);
                    responseModel.setMessage("[ServerResources] All server reached maxOnline");
                    responseModel.setMsg("服务器繁忙，请稍后重试");
                    return responseModel;
                }
                //比较 count 获取最多在线 server
                UserDto finalUserDto = getMaxOnlineServer(filterUserDto);

                ServerDto finalServer = getSpecificServer(activeServer, finalUserDto.getServer_id());
                if (null == finalServer){
                    responseModel.setCode(errorCode);
                    responseModel.setMessage("[ServerResources] Get max online server "+finalUserDto.getServer_id()+" serverInfo is empty, please check server setting");
                    responseModel.setMsg("服务器连接错误，请稍后重试");
                    return responseModel;
                }
                serverInfo.setServerId(finalServer.getServer_id());
                host = getIpHost(finalServer.getIp());
                if (null==host||host.isEmpty()){
                    responseModel.setCode(errorCode);
                    responseModel.setMsg("服务器连接错误，请稍后重试");
                    responseModel.setMessage("[ServerResources] get ip "+finalServer.getIp()+" mapping empty,please check setting");
                    return responseModel;
                }
                serverInfo.setServer_ip(host);
                serverInfo.setServer_port(finalServer.getPort());
                responseModel.setCode(successCode);
                responseModel.setData(serverInfo);


            }
        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            responseModel.setMsg("服务器连接错误，请稍后重试");
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

    private List<UserDto> getServerOnlineUsers( List<ServerDto> activeServer ){
        List<UserDto> activeUsers = new ArrayList<>();
        for (ServerDto activeUserDto:activeServer){

            UserDto userDto = new UserDto();
            userDto.setServer_id(activeUserDto.getServer_id());
            userDto.setUser_count(0);
            activeUsers.add(userDto);

        }
        List<UserDto> serverOnline = userService.getServerUsersByFlag(onlineFlag);
        for (UserDto userDto:serverOnline){
            for (UserDto active:activeUsers){
                if (userDto.getServer_id().equals(active.getServer_id())){
                    active.setUser_count(userDto.getUser_count());
                }
            }
        }


        return activeUsers;
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

    private ServerDto getSpecificServer(List<ServerDto> activeServer,Integer serverId){
        for (ServerDto serverDto :activeServer){
            if (serverId.equals(serverDto.getServer_id())){
                return serverDto;
            }
        }
        return null;
    }

}

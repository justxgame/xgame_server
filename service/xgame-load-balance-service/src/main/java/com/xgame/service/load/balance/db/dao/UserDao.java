package com.xgame.service.load.balance.db.dao;

import com.xgame.service.load.balance.db.dto.UserBasicDto;
import com.xgame.service.load.balance.db.dto.UserDto;

import java.util.List;

public interface UserDao extends BaseDao {
    public List<UserDto> getServerUsersByFlag(Integer flag);


    public Integer getServerIdByUser(String userName);
    public UserDto getServerCountByIdFlag(Integer serverId,Integer flag);
    public Integer getUserOnlineStatus(String userName);

    public String getUserPhone(String uid,String serverId);
    public UserBasicDto getUserBasic(String nickName);

}

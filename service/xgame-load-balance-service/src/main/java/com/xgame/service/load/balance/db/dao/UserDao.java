package com.xgame.service.load.balance.db.dao;

import com.xgame.service.load.balance.db.dto.UserDto;

import java.util.List;

public interface UserDao extends BaseDao {
    public List<UserDto> getOnlineUsers();

    public List<UserDto> getOfflineUsers();

    public Integer getServerIdByUser(String userName);


}

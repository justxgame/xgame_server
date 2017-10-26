package com.xgame.service.load.balance.service;

import com.xgame.service.load.balance.db.dao.UserDao;
import com.xgame.service.load.balance.db.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    public List<UserDto> getOnlineUsers(){return userDao.getOnlineUsers();}

    public List<UserDto> getOfflineUsers(){return userDao.getOfflineUsers();}

    public Integer getServerIdByUser(String userName){return userDao.getServerIdByUser(userName);}
}

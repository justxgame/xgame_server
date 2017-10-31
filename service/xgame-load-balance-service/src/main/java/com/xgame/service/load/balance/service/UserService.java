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
    public List<UserDto> getServerUsersByFlag(Integer flag){return userDao.getServerUsersByFlag(flag);}


    public Integer getServerIdByUser(String userName){return userDao.getServerIdByUser(userName);}

    public UserDto getServerCountByIdFlag(Integer serveId,Integer flag){return userDao.getServerCountByIdFlag(serveId,flag);}

    public Integer getUserOnlineStatus(String userName){
        return userDao.getUserOnlineStatus(userName);}
}

package com.xgame.service.load.balance.service;

import com.xgame.service.load.balance.db.dao.UserLoginDao;
import com.xgame.service.load.balance.db.dto.PhoneCodeDto;
import com.xgame.service.load.balance.db.dto.UserLoginDto;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UserLoginService {
    @Inject
    UserLoginDao userLoginDao;
    public void saveCode(String phone,String code,String inDate){
        userLoginDao.saveCode(phone,code,inDate);
    }

    public PhoneCodeDto getPhone(String phone) {
        return userLoginDao.getPhone(phone);
    }
    public void saveUserLoginInfo(UserLoginDto dto){
        userLoginDao.saveLoginInfo(dto);
    }

    public UserLoginDto getLoginInfo(String accountId, String token) {
        return userLoginDao.getLoginInfo(accountId, token);
    }

    public void updateLoginToken(String accountId, String token) {
        userLoginDao.updateLoginToken(accountId,token);
    }
}

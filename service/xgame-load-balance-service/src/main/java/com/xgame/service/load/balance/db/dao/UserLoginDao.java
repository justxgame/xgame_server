package com.xgame.service.load.balance.db.dao;

import com.xgame.service.load.balance.db.dto.PhoneCodeDto;
import com.xgame.service.load.balance.db.dto.UserLoginDto;

public interface UserLoginDao extends BaseDao {
    public void saveCode(String phone,String code,String indate);
    public PhoneCodeDto getPhone(String phone);
    public void saveLoginInfo(UserLoginDto dto);
    public void updateLoginToken(String accountId,String session_token);
    public UserLoginDto getLoginInfo(String accountId,String token);

    public UserLoginDto getLogInfoByUname(String accountId);
}

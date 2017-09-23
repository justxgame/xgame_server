package com.xgame.service.manager.service;

import com.xgame.service.manager.db.dao.TokenDao;
import com.xgame.service.manager.db.dto.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class TokenService {
    @Autowired
    private TokenDao tokenDao;

    public TokenDto getObjectByUserID(String user_id){
        return (TokenDto) tokenDao.getObjectByID(user_id);
    }

    public TokenDto getObjectByToken(String token){
        return (TokenDto) tokenDao.getObjectByToken(token);
    }

    public void deleteObjectByUserID(String user_id){
        tokenDao.deleteTokenByUserID(user_id);
    }

    public void saveToken(TokenDto tokenDto){
        tokenDao.saveObject(tokenDto);
    }

    public String getUserNameByToken(String token){return tokenDao.getUserNameByToken(token);
    }
}

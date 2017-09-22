package com.xgame.service.manager.db.dao;


import com.xgame.service.manager.db.dto.TokenDto;

/**
 */
public interface TokenDao extends BaseDao {

    TokenDto getObjectByToken(String token);

    void deleteTokenByUserID(String user_ID);
}

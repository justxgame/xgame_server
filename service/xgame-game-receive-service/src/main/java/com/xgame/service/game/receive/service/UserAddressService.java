package com.xgame.service.game.receive.service;

import com.xgame.service.game.receive.db.dao.UserAddressDao;
import com.xgame.service.game.receive.rest.model.user.UserAddressReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAddressService {
    @Autowired
    private UserAddressDao userAddressDao;
    public void saveObject(UserAddressReportModel model){
        userAddressDao.saveObject(model);
    }
}

package com.xgame.service.manager.service;

import com.xgame.service.manager.db.dao.BroadcastDao;
import com.xgame.service.manager.db.dto.BroadCastDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BroadcastService {
    @Autowired
    BroadcastDao broadcastDao;
    public void saveObject(BroadCastDto dto){
        broadcastDao.saveObject(dto);
    }
    public void deleteMsg(Long id ){
        broadcastDao.deleteById(id);
    }
    public List<BroadCastDto> getAll(){
        return broadcastDao.getAll();
    }
}

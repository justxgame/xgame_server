package com.xgame.service.manager.service;

import com.xgame.service.manager.db.dao.BroadcastDao;
import com.xgame.service.manager.db.dto.BroadCastDto;
import com.xgame.service.manager.db.dto.BroadCastRegularDto;
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
    public List<BroadCastRegularDto> getRegularTask(){return broadcastDao.getRegularTasks();}
    public void updateRegularTaskById(BroadCastRegularDto dto){
        broadcastDao.updateRegularTaskById(dto);
    }
    public void deleteRegularTaskById(Long transection){
        broadcastDao.deleteRegularTaskById(transection);
    }
    public void saveRegularTask(BroadCastRegularDto dto){
        broadcastDao.saveRegularTask(dto);
    }

    public BroadcastDao getBroadcastDao() {
        return broadcastDao;
    }
}

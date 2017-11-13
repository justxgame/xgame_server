package com.xgame.service.manager.service;

import com.xgame.service.manager.db.dao.BroadcastDao;
import com.xgame.service.manager.db.dto.BroadCastDto;
import com.xgame.service.manager.db.dto.BroadCastRegularDto;
import com.xgame.service.manager.db.dto.PushDto;
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
    public List<BroadCastRegularDto> getRegularTaskByType(Integer type){return broadcastDao.getRegularTasksByType(type);}
    public List<BroadCastRegularDto> getAllRegularTask(){return broadcastDao.getAllRegularTasks();}
    public void updateRegularTaskById(BroadCastRegularDto dto){
        broadcastDao.updateRegularTaskById(dto);
    }
    public void deleteRegularTaskById(Long transection){
        broadcastDao.deleteRegularTaskById(transection);
    }
    public void saveRegularTask(BroadCastRegularDto dto){
        broadcastDao.saveRegularTask(dto);
    }
    public void savePush(PushDto dto){ broadcastDao.savePush(dto);}
    public List<BroadCastDto> getAllBroadCast (Integer type){return broadcastDao.getAllBroadCast(type);}

    public BroadcastDao getBroadcastDao() {
        return broadcastDao;
    }
}

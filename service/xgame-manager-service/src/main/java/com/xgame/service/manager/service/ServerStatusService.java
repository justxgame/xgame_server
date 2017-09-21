package com.xgame.service.manager.service;


import com.xgame.service.manager.db.dao.ServerStatusDao;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerStatusService {
    @Autowired
    private ServerStatusDao serverStatusDao;
    public List<ServerStatusDto> getAll(){
        return serverStatusDao.getAll();
    }
    public void saveDto(ServerStatusDto dto){
        serverStatusDao.saveObject(dto);
    }
    public void updateDto(ServerStatusDto dto){
        serverStatusDao.updateObjectById(dto);
    }
    public void deleteById(Object id){
        serverStatusDao.deleteById(id);
    }
}

package com.xgame.service.manager.biz;


import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.manager.db.dto.BroadCastDto;
import com.xgame.service.manager.db.dto.BroadCastRegularDto;
import com.xgame.service.manager.db.dto.ServerStatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class BroadCastRegularBizProcessor extends BaseBiz {
    private static Logger logger = LoggerFactory.getLogger(BroadCastRegularBizProcessor.class.getName());
    public void broadCastTaskProcess(BroadCastRegularDto dto){

        try {
            if (null==dto){
                logger.warn("[BroadCastRegularBizProcessor] broadcast task is null");
                return;
            }
            //更新这轮任务开始时间
            dto.setStart_date(CommonUtil.getFormatDateByNow());

            logger.info("[BroadCastRegularBizProcessor] start processing task,BroadCastRegularDto ="+dto);

            List<ServerStatusDto> serverStatusDtos = serverStatusDao.getAllActive();
            requireNonNull(serverStatusDtos,"Get servers null");
            String serverId = dto.getServer_id();
            //全服发送
            if ("all".equalsIgnoreCase(serverId)){

                for (ServerStatusDto statusDto:serverStatusDtos){
                    //发送
                    sendBroadcast(statusDto,dto.getMsg());
                    BroadCastDto broadCastDto = new BroadCastDto();
                    broadCastDto.setServer_id(String.valueOf(statusDto.getServer_id()));
                    broadCastDto.setSend_user(dto.getUid());
                    broadCastDto.setIndate(CommonUtil.getFormatDateByNow());
                    broadCastDto.setMsg(dto.getMsg());
                    //保存到 广播历史表
                    broadcastDao.saveObject(broadCastDto);
                }
            }

            //获取 server
            ServerStatusDto statusDto = getDtoById(dto.getServer_id(), serverStatusDtos);
            if (null==statusDto){
                logger.error("[BroadCastRegularBizProcessor] get server null by server id"+dto.getServer_id());
                return;
            }
            //发送
            sendBroadcast(statusDto,dto.getMsg());

            BroadCastDto broadCastDto = new BroadCastDto();
            broadCastDto.setServer_id(String.valueOf(statusDto.getServer_id()));
            broadCastDto.setSend_user(dto.getUid());
            broadCastDto.setIndate(CommonUtil.getFormatDateByNow());
            broadCastDto.setMsg(dto.getMsg());
            //保存到 广播历史表
            broadcastDao.saveObject(broadCastDto);

            //更新上一轮任务结束时间
            dto.setEnd_date(CommonUtil.getFormatDateByNow());

            Long currentTime = CommonUtil.parseStr2Time(dto.getNext_send_date());
            Long nextTime = calculateNextDate(dto.getFreq_unit(), dto.getFreq_val(), currentTime);
            //更新下一轮发送时间
            dto.setNext_send_date(CommonUtil.getDsFromUnixTimestamp(nextTime));
            //更新定时任务表,更新相关时间
            broadcastDao.updateRegularTaskById(dto);



        }catch (Throwable t){
            logger.error("[BroadCastRegularBizProcessor] process task failed",t);
        }
    }
}

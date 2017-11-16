package com.xgame.service.manager.biz;

import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.manager.db.dto.BroadCastDto;
import com.xgame.service.manager.db.dto.BroadCastRegularDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushRegularBizProcessor extends BaseBiz {
    private static Logger logger = LoggerFactory.getLogger(PushRegularBizProcessor.class.getName());
    @Override
    public void broadCastTaskProcess(BroadCastRegularDto dto) {

        try {
            if (null==dto){
                logger.warn("[PushRegularBizProcessor] broadcast task is null");
                return;
            }
            dto.setStart_date(CommonUtil.getFormatDateByNow());

            logger.info("[PushRegularBizProcessor] start processing task,BroadCastRegularDto ="+dto);
            String date = CommonUtil.getFormatDateByNow();
            sendPush(dto.getMsg(),date);

            BroadCastDto broadCastDto = new BroadCastDto();

            broadCastDto.setSend_user(dto.getUid());
            broadCastDto.setIndate(date);
            broadCastDto.setMsg(dto.getMsg());
            broadCastDto.setType(dto.getType());
            //保存到 广播历史表
            //broadcastDao.saveObject(broadCastDto);

            //更新上一轮任务结束时间
            dto.setEnd_date(CommonUtil.getFormatDateByNow());

            Long currentTime = CommonUtil.parseStr2Time(dto.getNext_send_date());
            Long nextTime = calculateNextDate(dto.getFreq_unit(), dto.getFreq_val(), currentTime);
            //更新下一轮发送时间
            dto.setNext_send_date(CommonUtil.getDsFromUnixTimestamp(nextTime));
            //更新定时任务表,更新相关时间
            broadcastDao.updateRegularTaskById(dto);
        }catch (Throwable t){
            logger.error("[PushRegularBizProcessor] process task failed",t);
        }
    }
}

package com.xgame.order.consumer.biz;

import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogDao;
import com.xgame.order.consumer.db.dto.RewardBoxDto;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogDto;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.List;

public class BaseBiz {
    protected static CloseableHttpClient httpclient;
    {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(20 * 1000)
                .setConnectionRequestTimeout(20 * 1000)
                .setSocketTimeout(20 * 1000).build();
        httpclient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    }

    protected String getRewardInfo(int itemId, List<RewardBoxDto> rewardBoxDtos){
        for (RewardBoxDto dto:rewardBoxDtos){
            if (itemId==dto.getId()){
                return dto.getMemo();
            }
        }
        return null;
    }

    /**
     * 获取 10元话费的 10
     * @param rewardInfo
     * @return
     */
    protected String getChargeFee(String rewardInfo){
        int position = rewardInfo.lastIndexOf("0");
        return rewardInfo.substring(0, position+1);
    }

    protected RewardOrderInfoDto parsOrderLog2OrderInfo(RewardOrderLogDto dto,String status,String exception){
        RewardOrderInfoDto orderInfoDto = new RewardOrderInfoDto();
        orderInfoDto.setServerId(dto.getServer_id());
        orderInfoDto.setUid(dto.getUid());
        orderInfoDto.setIsReorder(dto.getIs_reorder());
        orderInfoDto.setItemCount(dto.getItem_count());
        orderInfoDto.setItemId(dto.getItem_id());
        orderInfoDto.setItemType(dto.getItem_type());
        orderInfoDto.setIndate(dto.getIndate());
        orderInfoDto.setOrderId(dto.getOrder_id());
        orderInfoDto.setOrderStatus(0);
        orderInfoDto.setReqId(dto.getOrder_id());
        orderInfoDto.setOrderException("");
        return orderInfoDto;
    }


}

package com.xgame.order.consumer.business;

import com.xgame.order.consumer.ServiceContextFactory;
import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogMappingDao;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.UnsupportedEncodingException;

/**
 * Created by william on 2017/9/26.
 */
public abstract class BaseBusiness {

    protected static CloseableHttpClient httpclient;

    static {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(20 * 1000)
                .setConnectionRequestTimeout(20 * 1000)
                .setSocketTimeout(20 * 1000).build();
        httpclient =
                HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }


    // 所有Dao定义
    protected  RewardOrderLogMappingDao rewardOrderLogMappingDao = ServiceContextFactory.getRewardOrderLogMappingDao();
    protected RewardOrderInfoDao rewardOrderInfoDao = ServiceContextFactory.getRewardOrderInfoDao();


    // 处理订单
    public abstract void processorOrder(RewardOrderLogMappingDto rewardOrderLogMappingDto) throws Throwable;


    /**
     * 转换
     * @param dto
     * @return
     */
    protected RewardOrderInfoDto parsOrderLog2OrderInfo(RewardOrderLogMappingDto dto) {
        RewardOrderInfoDto orderInfoDto = new RewardOrderInfoDto();
        orderInfoDto.setServer_id(dto.getServer_id());
        orderInfoDto.setUid(dto.getUid());
        orderInfoDto.setIs_reorder(dto.getIs_reorder());
        orderInfoDto.setItem_count(dto.getItem_count());
        orderInfoDto.setItem_id(dto.getItem_id());
        orderInfoDto.setItem_type(dto.getItem_type());
        orderInfoDto.setIndate(dto.getIndate());
        orderInfoDto.setOrder_id(dto.getOrder_id());
        orderInfoDto.setId(Integer.valueOf(dto.getId()));
        orderInfoDto.setPhone(dto.getPhone());
        return orderInfoDto;
    }


}

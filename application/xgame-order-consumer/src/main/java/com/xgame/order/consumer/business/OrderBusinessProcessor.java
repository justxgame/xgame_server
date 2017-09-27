package com.xgame.order.consumer.business;

import com.xgame.order.consumer.ServiceContextFactory;
import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogMappingDao;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import com.xgame.service.common.type.OrderInfoType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.*;

/**
 * Created by william on 2017/9/26.
 */
public class OrderBusinessProcessor {


    protected RewardOrderLogMappingDao rewardOrderLogMappingDao = ServiceContextFactory.getRewardOrderLogMappingDao();
    protected RewardOrderInfoDao rewardOrderInfoDao = ServiceContextFactory.getRewardOrderInfoDao();

    private static Logger logger = LoggerFactory.getLogger(OrderBusinessProcessor.class.getName());

    // 商品类目
    private static final int catalog_phone_direct = 100; // 手机直冲
    private static final int catalog_phone_card = 101;   // 手机卡密
    private static final int catalog_fuel_card = 201;    // 油卡卡密

    // 供货商类目
    private static final int provider_ofpay = 1; // 欧飞
    private static final int provider_pmi = 2; // 蜂助手


    // TODO 配置表
    private static Map<Integer, Map<Integer, String>> providerMapping = new HashMap(); // 所有供货商配置
    private static Map<Integer, String> ofPayCatalogClassMapping = new HashMap(); //欧飞实现
    private static Map<Integer, String> pmiCatalogClassMapping = new HashMap(); //蜂助手实现

    // 供货商实现配置
    static {
        providerMapping.put(provider_ofpay, ofPayCatalogClassMapping);
    }

    // 欧飞实现配置
    static {
        ofPayCatalogClassMapping.put(catalog_phone_direct, "com.xgame.order.consumer.business.ofpay.OfPayPhoneDirectBusiness");
        ofPayCatalogClassMapping.put(catalog_phone_card, "com.xgame.order.consumer.business.ofpay.OfPayPhoneCardBusiness");
        ofPayCatalogClassMapping.put(catalog_fuel_card, "com.xgame.order.consumer.business.ofpay.OfPayFuelCardBusiness");
    }


    public void processOrder(RewardOrderLogMappingDto rewardOrderLogMappingDto) {

        try {
            if (null == rewardOrderLogMappingDto) {
                logger.warn("[OrderBusinessProcessor] start process order is null");
                return;
            }
            logger.info("[OrderBusinessProcessor] start process order , rewardOrderLogMappingDto =  " + rewardOrderLogMappingDto.toString());

            // 检查订单合法性
            requireNonNull(rewardOrderLogMappingDto.getOrder_id(), "order id is null");
            String orderId = rewardOrderLogMappingDto.getOrder_id();
            logger.info("[OrderBusinessProcessor] start to process order , orderid=" + orderId);

            // 处理订单前，保存到info表 , 更新 order info表状态
            rewardOrderLogMappingDao.updateOrderToConsumer(rewardOrderLogMappingDto.getOrder_id());

            // 处理订单前，插入到info表 ,orderid 主键
            requireNonNull(rewardOrderLogMappingDto.getItem_count(), "item count is null");
            RewardOrderInfoDto rewardOrderInfoDto = parsOrderLog2OrderInfo(rewardOrderLogMappingDto);
            rewardOrderInfoDto.setOrder_status(OrderInfoType.INIT.getValue());
            rewardOrderInfoDto.setIndate(new Date());
            rewardOrderInfoDao.saveObject(rewardOrderInfoDto);

            requireNonNull(rewardOrderLogMappingDto.getProvider_id(), "该商品没有供货商");
            // 获取订单处理类
            Map<Integer, String> providerCatalogClassMapping = providerMapping.get(rewardOrderLogMappingDto.getProvider_id());
            if (null == providerCatalogClassMapping) {
                throw new RuntimeException(String.format("供货商不存在 ,  provide=%s not exist", rewardOrderLogMappingDto.getProvider_id()));
            }

            String processorClass = providerCatalogClassMapping.get(requireNonNull(rewardOrderLogMappingDto.getCatalog(), "商品类目不存在"));
            if (StringUtils.isEmpty(processorClass)) {
                throw new RuntimeException("供货商不提供该物品 , catalog = " + rewardOrderLogMappingDto.getCatalog());
            }

            BaseBusiness baseBusiness = (BaseBusiness) Class.forName(processorClass).newInstance();

            //处理订单
            baseBusiness.processorOrder(rewardOrderLogMappingDto, rewardOrderInfoDto);

            //更新info表状态
            rewardOrderInfoDao.updateObjectById(rewardOrderInfoDto);
            logger.info("[OrderBusinessProcessor] processor order over  , orderid=" + orderId);

        } catch (Throwable t) {
            logger.error("[OrderBusinessProcessor] start process order failed", t);
        } finally {

        }
    }


    /**
     * 转换
     *
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

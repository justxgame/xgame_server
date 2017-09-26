package com.xgame.order.consumer.business;

import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.*;

/**
 * Created by william on 2017/9/26.
 */
public class OrderBusinessProcessor {

    private static Logger logger = LoggerFactory.getLogger(OrderBusinessProcessor.class.getName());

    // 商品类目
    private static final int catalog_phone_direct = 100; // 手机直冲
    private static final int catalog_phone_card = 101;   // 手机卡密
    private static final int catalog_fuel_card = 201;    // 油卡卡密

    // 供货商类目
    private static final int provider_ofpay = 1; // 欧飞


    // TODO 配置表
    private static Map<Integer, Map<Integer, String>> providerMapping = new HashMap(); // 所有供货商配置
    private static Map<Integer, String> ofPayCatalogClassMapping = new HashMap(); //欧飞实现

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
            Map<Integer, String> providerCatalogClassMapping = providerMapping.get(requireNonNull(rewardOrderLogMappingDto.getProvider_id(), "provide id is empty"));
            String processorClass = providerCatalogClassMapping.get(requireNonNull(rewardOrderLogMappingDto.getCatalog(), "catalog is empty"));
            BaseBusiness baseBusiness = (BaseBusiness) Class.forName(processorClass).newInstance();
            baseBusiness.processorOrder(rewardOrderLogMappingDto);

        } catch (Throwable t) {
            logger.error("[OrderBusinessProcessor] start process order failed", t);
        } finally {

        }
    }

}

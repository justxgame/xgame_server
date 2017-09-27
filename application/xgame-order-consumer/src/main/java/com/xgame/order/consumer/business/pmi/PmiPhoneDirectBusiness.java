package com.xgame.order.consumer.business.pmi;

import com.xgame.order.consumer.conf.Configuration;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import com.xgame.service.common.type.OrderInfoType;
import com.xgame.service.common.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class PmiPhoneDirectBusiness extends AbstractPmiBusiness {
    private static Logger logger = LoggerFactory.getLogger(PmiPhoneDirectBusiness.class.getName());
    private final String url = Configuration.getInstance().getConfig().getString("pmi.phone.charge.url");
    private final String channelNo = Configuration.getInstance().getConfig().getString("pmi.channelno");
    private final String userId = Configuration.getInstance().getConfig().getString("pmi.userid");
    private final String userPw = Configuration.getInstance().getConfig().getString("pmi.userpw");
    private final String pwMd5 = CommonUtil.hashingMD5Lower(userPw);
    private final String recalUrl = Configuration.getInstance().getConfig().getString("pmi.phone.direct.recall.url");
    private final String version = "1.0";
    @Override
    public void processorOrder(RewardOrderLogMappingDto rewardOrderLogMappingDto, RewardOrderInfoDto rewardOrderInfoDto) throws Throwable {

        String exceptionMessage = "";
        String message = "";
        String res = "";


        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        for (int i = 0; i < rewardOrderLogMappingDto.getItem_count(); i++) {
            try {

                Integer price = requireNonNull(rewardOrderLogMappingDto.getPrice());
                //判断面额是否大于100
                String isLarge = isLarge(price);
                String orderId = requireNonNull(rewardOrderLogMappingDto.getOrder_id());
                String subOrderId = generateSubOrder(orderId, String.valueOf(i));
                String phone = requireNonNull(rewardOrderLogMappingDto.getPhone());
                String date = CommonUtil.getNormalDateByTime();
                String md5Str = CommonUtil.hashingMD5Lower(channelNo,pwMd5,phone,String.valueOf(price),isLarge,subOrderId,date,version,userId);

                // 调用接口
                HttpGet httpGet = new HttpGet(url);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("channelNo", channelNo));
                params.add(new BasicNameValuePair("userId", userId));
                params.add(new BasicNameValuePair("userpws", pwMd5));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("tariffeValue", String.valueOf(price)));
                params.add(new BasicNameValuePair("isLarge", isLarge));
                params.add(new BasicNameValuePair("orderId", subOrderId));
                params.add(new BasicNameValuePair("md5Str", md5Str));
                params.add(new BasicNameValuePair("retUrl", recalUrl));
                params.add(new BasicNameValuePair("txnDate", date));
                params.add(new BasicNameValuePair("version", version));

                logger.info("[PmiPhoneDirectBusiness] request params = " + getParameterStr(params));
                URI uri = new URIBuilder(httpGet.getURI()).addParameters(params).build();
                httpGet.setURI(uri);
                response = httpclient.execute(httpGet);
                entity = response.getEntity();
                res = EntityUtils.toString(entity);
                logger.info("[PmiPhoneDirectBusiness] res=" + res);
                message = res;
                //解析返回
                PmiOrderInfo orderInfo= getOrderInfo(res);
                requireNonNull(orderInfo, "orderinfo is null");
                if (isRechagering(requireNonNull(orderInfo.getReqcode()))){

                    message = message + String.format("第 %s 充值中 reqcode=%d", i, orderInfo.getReqcode());
                }else {
                    exceptionMessage = exceptionMessage + String.format("第 %s 充值失败 , return = %s", i, res);
                }

            }catch (Throwable t){
                logger.error("PmiPhoneDirectBusiness] request error ", t);
                exceptionMessage = exceptionMessage + ExceptionUtils.getMessage(t);
            }finally {
                try {
                    response.close();
                    EntityUtils.consume(entity);
                } catch (IOException e) {
                    logger.error("PmiPhoneDirectBusiness] request error ",e);
                    exceptionMessage = exceptionMessage + e.getMessage();
                }
            }

            // save to db
            if (StringUtils.isEmpty(exceptionMessage)) {
                rewardOrderInfoDto.setOrder_status(OrderInfoType.CHARGING.getValue());
            } else {
                rewardOrderInfoDto.setOrder_status(OrderInfoType.FAILURE.getValue());
            }
            rewardOrderInfoDto.setMessage(message);
            rewardOrderInfoDto.setOrder_exception(exceptionMessage);
        }

    }

    /**
     * 判断面额 大于100 0 小于100 1
     * @param price
     * @return
     */
    private String isLarge(Integer price){
        return price<100?"1":"0";
    }
    /**
     * 接口调用是否充值中
     *
     * @param reqcode
     * @return
     */
    protected boolean isRechagering(Integer reqcode) {

        return reqcode==0?true:false;
    }
}

package com.xgame.order.consumer.business.ofpay;

import com.alibaba.fastjson.JSONObject;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import com.xgame.order.consumer.rest.model.ExchangeResultModel;
import com.xgame.service.common.conf.Card;
import com.xgame.service.common.exception.CardChargeException;
import com.xgame.service.common.type.OrderInfoType;
import com.xgame.service.common.type.PhoneType;
import com.xgame.service.common.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Created by william on 2017/9/26.
 */
public class OfPayFuelCardBusiness extends AbstractOfPayBusiness {
    private static Logger logger = LoggerFactory.getLogger(OfPayFuelCardBusiness.class.getName());
    private final String request_url = ofpay_base_url + "/order.do";
    private final String mobinfo_url = ofpay_base_url + "/mobinfo.do";
    @Override
    public void processorOrder(RewardOrderLogMappingDto rewardOrderLogMappingDto,RewardOrderInfoDto rewardOrderInfoDto) {

        //更新 order info表 to consumer
        rewardOrderLogMappingDao.updateOrderToConsumer(rewardOrderLogMappingDto.getOrder_id());
        String exceptionMessage = "";
        String message = "";
        String res = "";


        HttpEntity entity =null;
        CloseableHttpResponse response=null;

        String pwd=null;
        try {
            // 提卡数量
            Integer cardnum = requireNonNull(rewardOrderLogMappingDto.getItem_count());
            // 订单
            String sporder_id = rewardOrderLogMappingDto.getOrder_id();
            //目前 extra 字段保存不同类型卡密的 cardid 移动|联通|电信
            String oriCardid = requireNonNull(rewardOrderLogMappingDto.getExtra());
            String phone = requireNonNull(rewardOrderLogMappingDto.getPhone());
            //调用归属地接口获取电话归属地
            PhoneType phoneType = getPhoneType(phone, mobinfo_url, httpclient);

            String sporder_time = getOFDateByNow();
            //TODO 油卡分种类
            String md5_str = null ;// CommonUtil.hashingMD5(userid, userpws, cardid, String.valueOf(cardnum), sporder_id, sporder_time, keyStr);

            HttpPost httpPost = new HttpPost(request_url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userid", userid));
            params.add(new BasicNameValuePair("userpws", userpws));
            params.add(new BasicNameValuePair("cardid", "")); //TODO
            params.add(new BasicNameValuePair("cardnum", String.valueOf(cardnum)));
            params.add(new BasicNameValuePair("sporder_id", sporder_id));
            params.add(new BasicNameValuePair("sporder_time", sporder_time));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("md5_str", md5_str));
            params.add(new BasicNameValuePair("version", version));
            logger.info("[OfPayFuelCardBusiness] request params = " + getParameterStr(params));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = httpclient.execute(httpPost);
            entity = response.getEntity();
            res = EntityUtils.toString(entity);
            message = res;
            logger.info("[OfPayFuelCardBusiness] res=" + res);
            OrderInfo orderInfo = getOrderInfo(res);
            requireNonNull(orderInfo, "orderInfo is empty");
            if (isSuccess(orderInfo)) {
                List<Card> cards = orderInfo.getCards().getCard();
                String carsStr = JSONObject.toJSONString(cards);
                pwd=carsStr;
                message = message + String.format("充值成功  retcode=%s cards=%s", orderInfo.getRetcode(),carsStr);
            } else {
                exceptionMessage = exceptionMessage + String.format("充值失败 , return = %s", res);
            }
        }catch (Throwable t){
            logger.error("OfPayFuelCardBusiness] request error ",t);
            exceptionMessage = exceptionMessage + ExceptionUtils.getMessage(t);
        }finally {
            try {
                if(null!=response){
                    response.close();
                }
                EntityUtils.consume(entity);
            } catch (IOException e) {
                logger.error("OfPayFuelCardBusiness] request failed ",e);
                exceptionMessage = exceptionMessage + e.getMessage();
            }

        }
        if(StringUtils.isEmpty(exceptionMessage)){
            rewardOrderInfoDto.setOrder_status(OrderInfoType.SUCCESS.getValue());
        }else{
            rewardOrderInfoDto.setOrder_status(OrderInfoType.FAILURE.getValue());
        }

        //如果成功， 回调游戏服务商
        if(rewardOrderInfoDto.getOrder_status().equals(OrderInfoType.SUCCESS.getValue())){
            ExchangeResultModel exchangeResultModel = new ExchangeResultModel();
            exchangeResultModel.setUid(Integer.valueOf(rewardOrderLogMappingDto.getUid()));
            exchangeResultModel.setServerId(Integer.valueOf(rewardOrderLogMappingDto.getServer_id()));
            exchangeResultModel.setId(Integer.valueOf(rewardOrderLogMappingDto.getId()));
            exchangeResultModel.setPassword(pwd);
            String url = rewardOrderLogMappingDto.getUrl();
            requireNonNull(url,"server url is null.can't call back");
            String gameUrl = HTTP_PREFIX+url+"/exchange_result";
            logger.info("[OfPayFuelCardBusiness] call url = " + gameUrl + ", exchangeResultModel=" + exchangeResultModel);
            try {
                gameCallBack(gameUrl,exchangeResultModel);
                message = message +String.format("game call back success  ");
            }catch (Throwable t){
                logger.error("OfPayFuelCardBusiness] callback error ",t);
                exceptionMessage = exceptionMessage + ExceptionUtils.getMessage(t);
            }
        }
        rewardOrderInfoDto.setMessage(message);
        rewardOrderInfoDto.setOrder_exception(exceptionMessage);

    }

    protected boolean isSuccess(OrderInfo orderInfo){
        if (orderInfo.getRetcode()==1&&orderInfo.getCards()!=null){
            return true;
        }else {
            return false;
        }
    }
}

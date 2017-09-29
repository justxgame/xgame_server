package com.xgame.order.consumer.business.pmi;

import com.alibaba.fastjson.JSONObject;
import com.xgame.order.consumer.conf.Configuration;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
import com.xgame.order.consumer.rest.model.pmi.FuelCardModel;
import com.xgame.order.consumer.rest.model.pmi.FuelCardResModel;
import com.xgame.order.consumer.rest.model.pmi.Params;
import com.xgame.service.common.type.OrderInfoType;
import com.xgame.service.common.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

import static java.util.Objects.requireNonNull;

public class PmiFuelCardBusiness extends AbstractPmiBusiness {
    private static Logger logger = LoggerFactory.getLogger(PmiFuelCardBusiness.class.getName());
    private final static String SIGN = "=";
    private final static String AND = "&";
    private final static String ORDER_NO="ORDER_NO";
    private final static String CARD_TYPE = "CARD_TYPE";
    private final static String CARD_TYPE_NAME = "CARD_TYPE_NAME";
    private final static String CARD_CODE = "CARD_CODE";
    private final static String PHONE_NO = "PHONE_NO";
    private final static String RECHARGE_AMOUNT = "RECHARGE_AMOUNT";
    private final static String RET_URL = "RET_URL";
    private final static String QUANTITY = "QUANTITY";
    private final static String APP_ID = "appid";
    private final static String REQ_ID = "reqid";
    private final static String CODE = "code";
    private final static String VERSION = "version";
    private final static String PARAMS = "params";
    private final static String TIME = "time";
    private final static String APP_KEY = "app_key";
    private final static String DESC = "desc";
    private final static String SUCCESS = "SUCCESS";
    private final String url = Configuration.getInstance().getConfig().getString("pmi.fuel.card.charge.url");

    private final String appid = Configuration.getInstance().getConfig().getString("pmi.fuel.card.charge.appid");
    private final String appkey = Configuration.getInstance().getConfig().getString("pmi.fuel.card.charge.appkey");
    private final String recallUrl = Configuration.getInstance().getConfig().getString("pmi.fuel.card.charge.recall.url");
    private final String version = "1.0";
    private final String code = "300100";
    private final String desc = "加油卡充值";
    private final String cardType = "KEY-SINOPEC";
    private final String cardTypeName="中石化卡密";
    @Override
    public void processorOrder(RewardOrderLogMappingDto rewardOrderLogMappingDto, RewardOrderInfoDto rewardOrderInfoDto) throws Throwable {
        String exceptionMessage = "";
        String message = "";
        String res = "";
        HttpEntity entity = null;
        CloseableHttpResponse response = null;

        // 循环 count
        for (int i = 0; i < rewardOrderLogMappingDto.getItem_count(); i++) {
            try {
                String reqId = CommonUtil.getOrderId(3);
                Integer price = requireNonNull(rewardOrderLogMappingDto.getPrice());
                if (!isCorrectPrice(price)){
                    throw new RuntimeException(" pmi fuel card can't support this price " + price);
                }
                String priceStr = String.valueOf(price);
                String orderId = requireNonNull(rewardOrderLogMappingDto.getOrder_id());
                String subOrderId = generateSubOrder(orderId, String.valueOf(i));
                String phone = requireNonNull(rewardOrderLogMappingDto.getPhone());

                Params params = getParams(cardType, cardTypeName, subOrderId, String.valueOf(i),
                        phone, priceStr, recallUrl);
                String paramsMd5 = getParamsMd5(params);
                String time = CommonUtil.getFormatDateByNow();
                String sign = getSign(reqId, appid, code, version, desc, time, appkey, paramsMd5);

                FuelCardModel fuelCardModel = getModel(reqId, appid, code, version, desc, time, sign, params);

                //http 调用
                HttpPost post = new HttpPost(url);
                post.addHeader("Content-type","application/json; charset=utf-8");
                post.setHeader("Accept", "application/json");
                String jsonStr = JSONObject.toJSONString(fuelCardModel);
                StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
                reqEntity.setContentEncoding("UTF-8");
                reqEntity.setContentType("application/json");
                post.setEntity(reqEntity);
                response = httpclient.execute(post);
                entity= response.getEntity();
                res = EntityUtils.toString(entity, "UTF-8");
                if (!StringUtils.isEmpty(res)){
                    res = CommonUtil.unicodeToString(res);
                }
                logger.info("[PmiFuelCardBusiness] res=" + res);
                message = res;
                //解析返回
                FuelCardResModel resModel = JSONObject.parseObject(res, FuelCardResModel.class);
                requireNonNull(resModel,"Pmi fuel card res model is null");
                if(SUCCESS.equals(resModel.getResult())){
                    message = message + String.format("第 %s 充值中 ", i);
                }else {
                    exceptionMessage = exceptionMessage + String.format("第 %s 充值失败 , return = %s", i, res);
                }

            }catch (Throwable t){
                logger.error("PmiFuelCardBusiness] request error ", t);
                exceptionMessage = exceptionMessage + ExceptionUtils.getMessage(t);
            }finally {
                try {
                    if (response!=null){
                        response.close();
                    }
                    EntityUtils.consume(entity);
                } catch (IOException e) {
                    logger.error("PmiFuelCardBusiness] request error ",e);
                    exceptionMessage = exceptionMessage + e.getMessage();
                }
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



    private String getParamsMd5(Params params){
        StringBuilder stringBuilder = new StringBuilder();
        String paramsStr = stringBuilder.append(CARD_TYPE).append(SIGN).append(params.getCARD_TYPE()).append(AND)
                .append(CARD_TYPE_NAME).append(SIGN).append(params.getCARD_TYPE_NAME()).append(AND)
                .append(ORDER_NO).append(SIGN).append(params.getORDER_NO()).append(AND)
                .append(PHONE_NO).append(SIGN).append(params.getPHONE_NO()).append(AND)
                .append(QUANTITY).append(SIGN).append(params.getQUANTITY()).append(AND)
                .append(RECHARGE_AMOUNT).append(SIGN).append(params.getRECHARGE_AMOUNT()).append(AND)
                .append(RET_URL).append(SIGN).append(params.getRET_URL()).toString();
        return CommonUtil.hashingMD5(paramsStr).toUpperCase();
    }

    private Params getParams(String cardType,String cardTypeName,String orderNo,String itemCount,String phone,String amount,
                             String recallUrl ){
        Params params = new Params();
        params.setCARD_TYPE(cardType);
        params.setCARD_TYPE_NAME(cardTypeName);
        params.setORDER_NO(orderNo);
        params.setPHONE_NO(phone);
        params.setQUANTITY(itemCount);
        params.setRECHARGE_AMOUNT(amount);
        params.setRET_URL(recallUrl);
        return params;
    }

    private String getSign(String reqid,String appid,String code,String version,String desc,String time,String appkey,String paramsMd5){
        StringBuilder sb = new StringBuilder();
        String str = sb.append(APP_ID).append(SIGN).append(appid).append(AND)
                .append(CODE).append(SIGN).append(code).append(AND)
                .append(DESC).append(SIGN).append(desc).append(AND)
                .append(PARAMS).append(SIGN).append(paramsMd5).append(AND)
                .append(REQ_ID).append(SIGN).append(reqid).append(AND)
                .append(TIME).append(SIGN).append(time).append(AND)
                .append(VERSION).append(SIGN).append(version).append(AND)
                .append(APP_KEY).append(SIGN).append(appkey).toString();
        return CommonUtil.hashingMD5(str).toUpperCase();

    }

    private FuelCardModel getModel(String reqId,String appid,String code,String version,String desc,
                                   String time,String sign,Params params){
        FuelCardModel fuelCardModel = new FuelCardModel();
        fuelCardModel.setAppid(appid);
        fuelCardModel.setCode(code);
        fuelCardModel.setDesc(desc);
        fuelCardModel.setParams(params);
        fuelCardModel.setReqid(reqId);
        fuelCardModel.setTime(time);
        fuelCardModel.setVersion(version);
        fuelCardModel.setSign(sign);
        return fuelCardModel;
    }

    /**
     * 油卡支持价格判断
     * @param price
     * @return
     */
    private boolean isCorrectPrice(Integer price){

        if(50==price||100==price||200==price||500==price||1000==price){
            return true;
        }
        return false;
    }


}

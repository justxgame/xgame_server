package com.xgame.order.consumer.biz;

import com.alibaba.fastjson.JSONObject;
import com.xgame.order.consumer.conf.Configuration;
import com.xgame.order.consumer.db.dao.RewardOrderInfoDao;
import com.xgame.order.consumer.db.dao.RewardOrderLogDao;
import com.xgame.order.consumer.db.dao.RewardUserDao;
import com.xgame.order.consumer.db.dto.RewardBoxDto;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.RewardOrderLogDto;
import com.xgame.order.consumer.rest.model.FuelCardModel;
import com.xgame.order.consumer.rest.model.Params;
import com.xgame.service.common.util.CommonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class FuelCardRechargeBiz extends BaseBiz{
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

    private SqlSession sqlSession;
    private List<RewardBoxDto> rewardBoxDtos;
    private static Logger logger = LoggerFactory.getLogger(FuelCardRechargeBiz.class);

    public FuelCardRechargeBiz(SqlSession sqlSession, List<RewardBoxDto> rewardBoxDtos){
        this.sqlSession = sqlSession;
        this.rewardBoxDtos=rewardBoxDtos;
    }


   public List<RewardOrderInfoDto> getProcessedResult(List<RewardOrderLogDto> rewardOrderLogDtos){
       if (rewardOrderLogDtos.isEmpty()){
           return Collections.EMPTY_LIST;
       }
       List<RewardOrderInfoDto> result = new ArrayList<>();
       String url = Configuration.getInstance().getConfig().getString("fuel.card.charge.url");
       String reqId = CommonUtil.getOrderId(3);
       String appid = Configuration.getInstance().getConfig().getString("fuel.card.charge.appid");
       String appkey = Configuration.getInstance().getConfig().getString("fuel.card.charge.appkey");
       String recallUrl = Configuration.getInstance().getConfig().getString("fuel.card.charge.recall.rul");
       String version = "1.0";
       String code = "300100";
       String desc = "加油卡充值";
       RewardUserDao userDao = sqlSession.getMapper(RewardUserDao.class);



       for (RewardOrderLogDto dto:rewardOrderLogDtos){
           String rewardInfo = getRewardInfo(dto.getItem_id(), rewardBoxDtos);
           if (null==rewardInfo){
              logger.error("can't get item memo. wrong item id");
              continue;
           }
           Map<String, Object> queryMap = new HashMap<>();
           queryMap.put("server_id", dto.getServer_id());
           queryMap.put("uid", dto.getUid());
           String phone = userDao.getObjectByID(queryMap);
           if (phone==null||phone.isEmpty()){
               logger.error("can't get phone by uid:"+dto.getUid()+" server_id"+dto.getServer_id());
               continue;
           }
           String chargeFee = getChargeFee(rewardInfo);

           HttpPost post = new HttpPost(url);
           post.addHeader("Content-type","application/json; charset=utf-8");
           post.setHeader("Accept", "application/json");

           Params params = getParams("KEY-SINOPEC", "中石化卡密", dto.getOrder_id(), String.valueOf(dto.getItem_count()),
                   phone, chargeFee, recallUrl);
           String paramsMd5 = getParamsMd5(params);
           String time = dto.getIndate();
           String sign = getSign(reqId, appid, code, version, desc, time, appkey, paramsMd5);

           FuelCardModel fuelCardModel = new FuelCardModel();
           fuelCardModel.setAppid(appid);
           fuelCardModel.setCode(code);
           fuelCardModel.setDesc(desc);
           fuelCardModel.setParams(params);
           fuelCardModel.setReqid(reqId);
           fuelCardModel.setTime(time);
           fuelCardModel.setVersion(version);
           fuelCardModel.setSign(sign);
           String jsonStr = JSONObject.toJSONString(fuelCardModel);
           StringEntity reqEntity = new StringEntity(jsonStr, Charset.forName("UTF-8"));
           reqEntity.setContentEncoding("UTF-8");
           reqEntity.setContentType("application/json");

           post.setEntity(reqEntity);
           CloseableHttpResponse response =null;
           HttpEntity entity=null;
           try {
               response = httpclient.execute(post);
               entity= response.getEntity();
               String res = EntityUtils.toString(entity, "UTF-8");
               System.out.println(res);
           } catch (IOException e) {
               e.printStackTrace();
           }finally {
               try {

                   response.close();
                   EntityUtils.consume(entity);
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

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
           RewardOrderLogDao orderLogDao = sqlSession.getMapper(RewardOrderLogDao.class);
           orderLogDao.updateObjectById(dto);

           result.add(orderInfoDto);
       }


       return result;
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


}

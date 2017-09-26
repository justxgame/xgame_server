//package com.xgame.order.consumer.biz;
//
//import com.alibaba.fastjson.JSONObject;
//import com.xgame.order.consumer.conf.Configuration;
//import com.xgame.order.consumer.db.dao.RewardOrderLogMappingDao;
//import com.xgame.order.consumer.db.dao.RewardUserDao;
//import com.xgame.order.consumer.db.dto.RewardBoxDto;
//import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
//import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
//import com.xgame.order.consumer.rest.model.ExchangeResultModel;
//import com.xgame.order.consumer.rest.model.Params;
//import com.xgame.service.common.conf.Card;
//import com.xgame.service.common.conf.ConfigHolder;
//import com.xgame.service.common.conf.OrderInfo;
//import com.xgame.service.common.util.CommonUtil;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.apache.ibatis.session.SqlSession;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.*;
//
//public class FuelCardRechargeBiz extends BaseBiz{
//    private final static String SIGN = "=";
//    private final static String AND = "&";
//    private final static String ORDER_NO="ORDER_NO";
//    private final static String CARD_TYPE = "CARD_TYPE";
//    private final static String CARD_TYPE_NAME = "CARD_TYPE_NAME";
//    private final static String CARD_CODE = "CARD_CODE";
//    private final static String PHONE_NO = "PHONE_NO";
//    private final static String RECHARGE_AMOUNT = "RECHARGE_AMOUNT";
//    private final static String RET_URL = "RET_URL";
//    private final static String QUANTITY = "QUANTITY";
//    private final static String APP_ID = "appid";
//    private final static String REQ_ID = "reqid";
//    private final static String CODE = "code";
//    private final static String VERSION = "version";
//    private final static String PARAMS = "params";
//    private final static String TIME = "time";
//    private final static String APP_KEY = "app_key";
//    private final static String DESC = "desc";
//
//    private SqlSession sqlSession;
//    private List<RewardBoxDto> rewardBoxDtos;
//    private static Logger logger = LoggerFactory.getLogger(FuelCardRechargeBiz.class);
//
//    public FuelCardRechargeBiz(SqlSession sqlSession, List<RewardBoxDto> rewardBoxDtos){
//        this.sqlSession = sqlSession;
//        this.rewardBoxDtos=rewardBoxDtos;
//    }
//
//
//   public List<RewardOrderInfoDto> getProcessedResult(List<RewardOrderLogMappingDto> rewardOrderLogDtos) throws URISyntaxException {
//       if (rewardOrderLogDtos.isEmpty()){
//           return Collections.EMPTY_LIST;
//       }
//       List<RewardOrderInfoDto> result = new ArrayList<>();
//       String url = Configuration.getInstance().getConfig().getString("ofpay.charge.url");
//       String cardId = Configuration.getInstance().getConfig().getString("ofpay.fuel.cardid");
//       String userId = Configuration.getInstance().getConfig().getString("ofpay.userid");
//       String userPw = Configuration.getInstance().getConfig().getString("ofpay.userpw");
//       String keyStr = Configuration.getInstance().getConfig().getString("ofpay.keystr");
//       String pwMd5 = CommonUtil.hashingMD5(userPw);
//       String version = "6.0";
//
//       RewardUserDao userDao = sqlSession.getMapper(RewardUserDao.class);
//       RewardOrderLogMappingDao rewardOrderLogDao = sqlSession.getMapper(RewardOrderLogMappingDao.class);
//       for (RewardOrderLogMappingDto dto:rewardOrderLogDtos){
//
//           String rewardInfo = getRewardInfo(dto.getItem_id(), rewardBoxDtos);
//           if (null==rewardInfo){
//              logger.error("can't get item memo. wrong item id");
//              continue;
//           }
//           ExchangeResultModel resultModel = new ExchangeResultModel();
//           resultModel.setId(Integer.valueOf(dto.getId()));
//           resultModel.setUid(Integer.valueOf(dto.getUid()));
//           resultModel.setServerId(Integer.valueOf(dto.getServer_id()));
//
//           HttpGet request = new HttpGet(url);
//
//           String itemCount = String.valueOf(dto.getItem_count());
//           String orderId = dto.getOrder_id();
//           String date = CommonUtil.getOFDateByNow();
//
//           String chargeFee = getChargeFee(rewardInfo);
//           String md5Str = getMd5(userId, pwMd5, cardId, itemCount, orderId, date, keyStr);
//           String md5 = CommonUtil.hashingMD5(md5Str).toUpperCase();
//
//           List<NameValuePair> nameValuePairs = new ArrayList<>();
//           NameValuePair userIdPair = new BasicNameValuePair("userid", userId);
//           NameValuePair pwPair = new BasicNameValuePair("userpws", pwMd5);
//           NameValuePair cardidPair = new BasicNameValuePair("cardid", cardId);
//           NameValuePair cardnumPair = new BasicNameValuePair("cardnum", chargeFee);
//           NameValuePair orderIdPair = new BasicNameValuePair("sporder_id", orderId);
//           NameValuePair datePair = new BasicNameValuePair("sporder_time", date);
//
//           NameValuePair md5Pair = new BasicNameValuePair("md5_str", md5);
//           NameValuePair versionPair = new BasicNameValuePair("version", version);
//
//
//           nameValuePairs.add(userIdPair);
//           nameValuePairs.add(pwPair);
//           nameValuePairs.add(cardidPair);
//           nameValuePairs.add(orderIdPair);
//           nameValuePairs.add(versionPair);
//           nameValuePairs.add(md5Pair);
//           nameValuePairs.add(datePair);
//           nameValuePairs.add(cardnumPair);
//           URI uri = new URIBuilder(request.getURI()).addParameters(nameValuePairs).build();
//           CloseableHttpResponse response = null;
//           request.setURI(uri);
//           HttpEntity entity=null;
//           int status = 0;
//           String exception = "";
//           try {
//               response = httpclient.execute(request);
//               entity = response.getEntity();
//               String res = EntityUtils.toString(entity);
//               System.out.println(res);
//               OrderInfo orderInfo = ConfigHolder.getOrderInfo(res);
//               if (1!=orderInfo.getRetcode()){
//                   resultModel.setPassword("error "+orderInfo.getErr_msg());
//               }else {
//                   List<Card> cards = orderInfo.getCards().getCard();
//                   String jsonCard = JSONObject.toJSONString(cards);
//                   resultModel.setPassword(jsonCard);
//
//               }
//
//
//
//           } catch (Throwable t) {
//               logger.error(ExceptionUtils.getMessage(t));
//           }finally {
//               try {
//                   response.close();
//                   EntityUtils.consume(entity);
//               } catch (IOException e) {
//                   logger.error(e.getMessage());
//               }
//           }
//
//           resultModel.setPassword("success");
//
//           //TODO url 获取
//           String gameServer="http://117.8.50.212:10009"+"/exchange_result";
//           int code = postResultCode(httpclient, url, resultModel);
//           rewardOrderLogDao.updateObjectById(dto);
//           RewardOrderInfoDto orderInfoDto = parsOrderLog2OrderInfo(dto, status, exception);
//
//           result.add(orderInfoDto);
//       }
//
//
//       return result;
//   }
//
//   private String getParamsMd5(Params params){
//       StringBuilder stringBuilder = new StringBuilder();
//       String paramsStr = stringBuilder.append(CARD_TYPE).append(SIGN).append(params.getCARD_TYPE()).append(AND)
//               .append(CARD_TYPE_NAME).append(SIGN).append(params.getCARD_TYPE_NAME()).append(AND)
//               .append(ORDER_NO).append(SIGN).append(params.getORDER_NO()).append(AND)
//               .append(PHONE_NO).append(SIGN).append(params.getPHONE_NO()).append(AND)
//               .append(QUANTITY).append(SIGN).append(params.getQUANTITY()).append(AND)
//               .append(RECHARGE_AMOUNT).append(SIGN).append(params.getRECHARGE_AMOUNT()).append(AND)
//               .append(RET_URL).append(SIGN).append(params.getRET_URL()).toString();
//       return CommonUtil.hashingMD5(paramsStr).toUpperCase();
//   }
//
//   private Params getParams(String cardType,String cardTypeName,String orderNo,String itemCount,String phone,String amount,
//                            String recallUrl ){
//       Params params = new Params();
//       params.setCARD_TYPE(cardType);
//       params.setCARD_TYPE_NAME(cardTypeName);
//       params.setORDER_NO(orderNo);
//       params.setPHONE_NO(phone);
//       params.setQUANTITY(itemCount);
//       params.setRECHARGE_AMOUNT(amount);
//       params.setRET_URL(recallUrl);
//       return params;
//   }
//
//   private String getSign(String reqid,String appid,String code,String version,String desc,String time,String appkey,String paramsMd5){
//       StringBuilder sb = new StringBuilder();
//       String str = sb.append(APP_ID).append(SIGN).append(appid).append(AND)
//               .append(CODE).append(SIGN).append(code).append(AND)
//               .append(DESC).append(SIGN).append(desc).append(AND)
//               .append(PARAMS).append(SIGN).append(paramsMd5).append(AND)
//               .append(REQ_ID).append(SIGN).append(reqid).append(AND)
//               .append(TIME).append(SIGN).append(time).append(AND)
//               .append(VERSION).append(SIGN).append(version).append(AND)
//               .append(APP_KEY).append(SIGN).append(appkey).toString();
//       return CommonUtil.hashingMD5(str).toUpperCase();
//
//   }
//    private String getMd5(String userId,String pwMd5,String cardId,String cardNum,String orderId,
//                          String orderDate,String keystr){
//        StringBuilder sb = new StringBuilder();
//        String str = sb.append(userId).append(pwMd5).append(cardId).append(cardNum).append(orderId)
//                .append(orderDate).append(keystr).toString();
//        return str;
//    }
//
//}

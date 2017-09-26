//package com.xgame.order.consumer.biz;
//
//import com.xgame.order.consumer.conf.Configuration;
//import com.xgame.order.consumer.db.dao.RewardOrderLogMappingDao;
//import com.xgame.order.consumer.db.dao.RewardUserDao;
//import com.xgame.order.consumer.db.dto.RewardBoxDto;
//import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
//import com.xgame.order.consumer.db.dto.RewardOrderLogMappingDto;
//import com.xgame.order.consumer.rest.model.ExchangeResultModel;
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
//public class PhoneReChargeBiz extends BaseBiz {
//    private SqlSession sqlSession;
//    private List<RewardBoxDto> rewardBoxDtos;
//    private static Logger logger = LoggerFactory.getLogger(PhoneReChargeBiz.class.getName());
//    public PhoneReChargeBiz(SqlSession sqlSession,List<RewardBoxDto> rewardBoxDtos){
//        this.sqlSession = sqlSession;
//        this.rewardBoxDtos= rewardBoxDtos;
//    }
//    public List<RewardOrderInfoDto> getProcessedResult(List<RewardOrderLogMappingDto> rewardOrderLogDtos) throws URISyntaxException {
//        if (rewardOrderLogDtos.isEmpty()){
//            return Collections.EMPTY_LIST;
//        }
//        List<RewardOrderInfoDto> result = new ArrayList<>();
//        String url = Configuration.getInstance().getConfig().getString("ofpay.charge.url");
//        String cardId = Configuration.getInstance().getConfig().getString("ofpay.phone.charge.direct.cardid");
//        String userId = Configuration.getInstance().getConfig().getString("ofpay.userid");
//        String userPw = Configuration.getInstance().getConfig().getString("ofpay.userpw");
//        String recalUrl = Configuration.getInstance().getConfig().getString("ofpay.phone.recall.url");
//        String keyStr = Configuration.getInstance().getConfig().getString("ofpay.keystr");
//        String pwMd5 = CommonUtil.hashingMD5(userPw);
//
//
//        RewardOrderLogMappingDao rewardOrderLogDao = sqlSession.getMapper(RewardOrderLogMappingDao.class);
//
//        RewardUserDao userDao = sqlSession.getMapper(RewardUserDao.class);
//        for (RewardOrderLogMappingDto dto:rewardOrderLogDtos){
//            String rewardInfo = getRewardInfo(dto.getItem_id(), rewardBoxDtos);
//            if (null==rewardInfo){
//                logger.error("can't get item memo. wrong item id");
//                continue;
//            }
//            ExchangeResultModel resultModel = new ExchangeResultModel();
//            resultModel.setId(Integer.valueOf(dto.getId()));
//            resultModel.setUid(Integer.valueOf(dto.getUid()));
//            resultModel.setServerId(Integer.valueOf(dto.getServer_id()));
//
//
//            //TODO count>1 需要循环
//            String itemCount = String.valueOf(dto.getItem_count());
//            HttpGet request = new HttpGet(url);
//            String chargeFee = getChargeFee(rewardInfo);
//            String phone=dto.getPhone();
//
//
//
//            String version = "6.0";
//
//            String date= null;
//            try {
//                date=CommonUtil.getOFDateByNow();
//            } catch (Throwable t) {
//               logger.error(ExceptionUtils.getMessage(t));
//               continue;
//            }
//
//            String orderId = dto.getOrder_id();
//
//
//            List<NameValuePair> nameValuePairs = new ArrayList<>();
//            NameValuePair userIdPair = new BasicNameValuePair("userid", userId);
//            NameValuePair pwPair = new BasicNameValuePair("userpws", pwMd5);
//            NameValuePair cardidPair = new BasicNameValuePair("cardid", cardId);
//            NameValuePair cardnumPair = new BasicNameValuePair("cardnum", chargeFee);
//            NameValuePair orderIdPair = new BasicNameValuePair("sporder_id", orderId);
//            NameValuePair datePair = new BasicNameValuePair("sporder_time", date);
//            NameValuePair phonePair = new BasicNameValuePair("game_userid",phone );
//
//            String key = getMd5(userId,pwMd5,cardId,chargeFee,orderId,date,phone,keyStr);
//            NameValuePair md5Pair = new BasicNameValuePair("md5_str", CommonUtil.hashingMD5(key).toUpperCase());
//            NameValuePair versionPair = new BasicNameValuePair("version", version);
//            NameValuePair retUrlPair = new BasicNameValuePair("ret_url", recalUrl);
//
//            nameValuePairs.add(userIdPair);
//            nameValuePairs.add(phonePair);
//            nameValuePairs.add(pwPair);
//            nameValuePairs.add(cardidPair);
//            nameValuePairs.add(orderIdPair);
//            nameValuePairs.add(versionPair);
//            nameValuePairs.add(md5Pair);
//            nameValuePairs.add(datePair);
//            nameValuePairs.add(retUrlPair);
//            nameValuePairs.add(cardnumPair);
//            URI uri = new URIBuilder(request.getURI()).addParameters(nameValuePairs).build();
//            CloseableHttpResponse response = null;
//            request.setURI(uri);
//            HttpEntity entity=null;
//            int status = 0;
//            String exception = "";
//            try {
//                response = httpclient.execute(request);
//                entity = response.getEntity();
//                String res = EntityUtils.toString(entity);
//                System.out.println(res);
//                OrderInfo orderInfo = ConfigHolder.getOrderInfo(res);
//                if (1!=orderInfo.getRetcode()){
//                   resultModel.setPassword("error ");
//                }
////                OrderInfo orderInfo = ConfigHolder.getPhoneChargeXml(res);
////                if (0!=orderInfo.getReqcode()){
////                    status=1;
////                    exception = orderInfo.getMsg();
////                }
//
//
//            } catch (Throwable t) {
//               logger.error(ExceptionUtils.getMessage(t));
//            }finally {
//                try {
//                    response.close();
//                    EntityUtils.consume(entity);
//                } catch (IOException e) {
//                   logger.error(e.getMessage());
//                }
//            }
//
//            resultModel.setPassword("success");
//
//            //TODO url 获取
//            String gameServer="http://117.8.50.212:10009"+"/exchange_result";
//             int code = postResultCode(httpclient, url, resultModel);
//              rewardOrderLogDao.updateObjectById(dto);
//              RewardOrderInfoDto orderInfoDto = parsOrderLog2OrderInfo(dto, status, exception);
//              result.add(orderInfoDto);
//
//        }
//
//        return result;
//    }
//
//    private String getKey(String channel,String pwMd5,String phone,String feeValue,String isLarge,String orderId,String date,
//                          String version,String userId){
//        StringBuilder stringBuilder = new StringBuilder();
//        String md5 = stringBuilder.append(channel).append(pwMd5).append(phone).append(feeValue).append(isLarge)
//                .append(orderId).append(date).append(version).append(userId).toString();
//        return md5;
//    }
//
//
//    private String getIsLarge(String chargeValue){
//        if (Integer.valueOf(chargeValue)<100){
//            return "1";
//        }
//        return "0";
//
//    }
//
//    private String getMd5(String userId,String pwMd5,String cardId,String cardNum,String orderId,
//                          String orderDate,String phone,String keystr){
//        StringBuilder sb = new StringBuilder();
//        String str = sb.append(userId).append(pwMd5).append(cardId).append(cardNum).append(orderId)
//                .append(orderDate).append(phone).append(keystr).toString();
//        return str;
//    }
//
//
//
//
//
//}

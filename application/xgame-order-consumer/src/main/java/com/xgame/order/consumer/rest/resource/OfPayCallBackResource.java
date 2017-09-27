package com.xgame.order.consumer.rest.resource;

import com.xgame.order.consumer.ServiceContextFactory;
import com.xgame.order.consumer.business.ofpay.AbstractOfPayBusiness;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.SubOrderInfoDto;
import com.xgame.order.consumer.rest.model.ExchangeResultModel;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.common.type.OrderInfoType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

import static java.util.Objects.*;

/**
 * Created by william on 2017/9/26.
 */
@Path("/ofpay")
public class OfPayCallBackResource extends BaseResources {

    private static Logger logger = LoggerFactory.getLogger(OfPayCallBackResource.class.getName());


    /**
     * 电话卡直冲，回调
     * <p>
     * 系统请求参数：ret_code 充值后状态，1代表成功，9代表撤消
     * <p>
     * <p>
     * <p>
     * ：ret_code=1&sporder_id=test001234567&ordersuccesstime=20160817140214&err_msg=
     *
     * @return
     */
    @POST
    @Path("/callback/phone/direct")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel phoneDirectCallback(@FormParam("ret_code") String ret_code, @FormParam("sporder_id") String sporder_id,
                                                 @FormParam("ordersuccesstime") String ordersuccesstime, @FormParam("err_msg") String err_msg) {
        logger.info(String.format("[ofpay:phoneDirectCallback] ret_code=%s, sporder_id=%s,ordersuccesstime=%s,err_msg=%s", ret_code, sporder_id, ordersuccesstime, err_msg));
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {

            requireNonNull(sporder_id, "sporder_id is empty");
            String orderStrs[] = sporder_id.split("_");
            if (orderStrs.length != 2) {
                throw new RuntimeException("sporder_id is not correct ,sporder_id =" + sporder_id);
            }


            String orderId = orderStrs[0];
            String batchNumber = orderStrs[1];
            //保存子订单表
            SubOrderInfoDto subOrderInfoDto = new SubOrderInfoDto();
            subOrderInfoDto.setSub_order_id(sporder_id);
            subOrderInfoDto.setOrder_id(orderId);
            subOrderInfoDto.setBatch_number(Integer.parseInt(batchNumber));
            subOrderInfoDto.setMessage(err_msg);
            subOrderInfoDto.setIndate(new Date());

            RewardOrderInfoDto rewardOrderInfoDto = (RewardOrderInfoDto) rewardOrderInfoDao.getObjectByID(orderId);
            requireNonNull(rewardOrderInfoDto, "parent order_id is not exist , order_id = " + orderId);

            // 判断返回值
            if (ret_code.equals("1")) {
                subOrderInfoDto.setState(OrderInfoType.SUCCESS.getValue());
            } else if (ret_code.equals("9")) {
                subOrderInfoDto.setState(OrderInfoType.CANCEL.getValue());
                rewardOrderInfoDto.setOrder_status(OrderInfoType.FAILURE.getValue());
                rewardOrderInfoDto.setOrder_exception(rewardOrderInfoDto.getMessage() + ", suborder cancel , sub_order_id = " + sporder_id);
            } else {
                subOrderInfoDto.setState(OrderInfoType.FAILURE.getValue());
                subOrderInfoDto.setMessage(subOrderInfoDto.getMessage() + "\n ,  res_code = " + ret_code);
                rewardOrderInfoDto.setOrder_status(OrderInfoType.FAILURE.getValue());
                rewardOrderInfoDto.setOrder_exception(rewardOrderInfoDto.getMessage() + ", suborder failure , sub_order_id = " + sporder_id);
            }
            //更新order表
            rewardOrderInfoDao.updateObjectById(rewardOrderInfoDto);
            //保存sub order表
            subOrderInfoDao.saveObject(subOrderInfoDto);

            //对父订单进行判断，是否已经全部成功
            Integer successSubOrderNumber = subOrderInfoDao.countStateByOrderId(orderId, OrderInfoType.SUCCESS.getValue());
            logger.info("[ofpay:phoneDirectCallback] order_id=" + orderId + ", successSubOrderNumber= " + successSubOrderNumber + ", total is " + rewardOrderInfoDto.getItem_count());

            // 已经全部成功
            if (successSubOrderNumber >= rewardOrderInfoDto.getItem_count()) {
                logger.info("[ofpay:phoneDirectCallback] all successful , send order_id to game server ,order_id = " + orderId);
                String serverUrl = rewardOrderInfoDao.getServerUrlByOrderId(orderId);
                if (StringUtils.isEmpty(serverUrl)) {
                    throw new RuntimeException("server url is empty");
                }
                if (null == rewardOrderInfoDto) {
                    throw new RuntimeException("rewardOrderInfoDto is not exist , order_Id = " + orderId);
                }
                rewardOrderInfoDto.setOrder_status(OrderInfoType.SUCCESS.getValue());
                try {
                    // callback
                    //回调游戏服务商
                    //生成 model
                    ExchangeResultModel exchangeResultModel = new ExchangeResultModel();
                    exchangeResultModel.setUid(Integer.parseInt(rewardOrderInfoDto.getUid()));
                    exchangeResultModel.setServerId(Integer.valueOf(rewardOrderInfoDto.getServer_id()));
                    exchangeResultModel.setId(Integer.valueOf(rewardOrderInfoDto.getId()));
                    exchangeResultModel.setPassword("充值话费全部成功");
                    String gameUrl = HTTP_PREFIX + serverUrl + "/exchange_result";
                    logger.info("[ofpay:phoneDirectCallback] call url = " + gameUrl + ", exchangeResultModel=" + exchangeResultModel);
                    AbstractOfPayBusiness.gameCallBack(gameUrl, exchangeResultModel);
                    rewardOrderInfoDto.setMessage(rewardOrderInfoDto.getMessage() + ",  game call back success");
                } catch (Throwable t) {
                    rewardOrderInfoDto.setMessage(rewardOrderInfoDto.getMessage() + ",  game call back failed");
                }
                rewardOrderInfoDao.updateObjectById(rewardOrderInfoDto);
            }

            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[ofpay:phoneDirectCallback] failed", t);
        }
        return wrapResponseModel;
    }
}

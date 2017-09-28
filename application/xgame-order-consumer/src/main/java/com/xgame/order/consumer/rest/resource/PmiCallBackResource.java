package com.xgame.order.consumer.rest.resource;

import com.alibaba.fastjson.JSONObject;
import com.xgame.order.consumer.business.ofpay.AbstractOfPayBusiness;
import com.xgame.order.consumer.business.pmi.PmiOrderInfo;
import com.xgame.order.consumer.business.pmi.Util;
import com.xgame.order.consumer.db.dto.RewardOrderInfoDto;
import com.xgame.order.consumer.db.dto.SubOrderInfoDto;
import com.xgame.order.consumer.rest.model.ExchangeResultModel;
import com.xgame.order.consumer.rest.model.pmi.Card;
import com.xgame.order.consumer.rest.model.pmi.FuelCardCallBackModel;
import com.xgame.order.consumer.rest.model.pmi.FuelCardCallBackResModel;
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
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * 蜂助手，回调方法
 * Created by william on 2017/9/26.
 */
@Path("/pmi")
public class PmiCallBackResource extends BaseResources {
    private final static String SUCCESS="SUCCESS";
    private final static String ERROR = "ERROR";
    private final static String RECHAGE_OK="RECHAGE_OK";


    private static Logger logger = LoggerFactory.getLogger(PmiCallBackResource.class.getName());


    /**
     * 电话卡直冲，回调
     * <p>
     * <p>
     * <?xml version="1.0" encoding=" UTF-8" ?>
     * - <orderinfo>
     *   <msg>应答说明</msg>
     * <retcode>交易结果响应码</ retcode >
     *   <orderId> SP交易流水号</orderId>
     * </orderinfo>
     * <p>
     * ：retcode只有返回1：充值成功，2：充值失败两种状态
     *
     * @return
     */
    @POST
    @Path("/callback/phone/direct")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel phoneDirectCallback(String body) {
        logger.info(String.format("[pmi:phoneDirectCallback] body=" + body));
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            if(StringUtils.isEmpty(body)){
                throw new RuntimeException("request body is empty");
            }
            PmiOrderInfo pmiOrderInfo = Util.getOrderInfo(body);
            requireNonNull(pmiOrderInfo,"pmiOrderInfo is null");

            String subOrderId = requireNonNull(pmiOrderInfo.getOrderId(), "order is not exist");
            String msg = pmiOrderInfo.getMsg();
            Integer retCode = requireNonNull(pmiOrderInfo.getRetcode(), "retCode is null");

            String orderStrs[] = subOrderId.split("_");
            if (orderStrs.length != 2) {
                throw new RuntimeException("sporder_id is not correct ,subOrderId =" + subOrderId);
            }
            String orderId = orderStrs[0];
            String batchNumber = orderStrs[1];

            //保存子订单表
            SubOrderInfoDto subOrderInfoDto = new SubOrderInfoDto();
            subOrderInfoDto.setSub_order_id(subOrderId);
            subOrderInfoDto.setOrder_id(orderId);
            subOrderInfoDto.setBatch_number(Integer.parseInt(batchNumber));
            subOrderInfoDto.setMessage(msg);
            subOrderInfoDto.setIndate(new Date());

            RewardOrderInfoDto rewardOrderInfoDto = (RewardOrderInfoDto) rewardOrderInfoDao.getObjectByID(orderId);
            requireNonNull(rewardOrderInfoDto, "parent order_id is not exist , order_id = " + orderId);

            // 判断返回值
            if (retCode.equals(1)) {
                subOrderInfoDto.setState(OrderInfoType.SUCCESS.getValue());
            } else {
                subOrderInfoDto.setState(OrderInfoType.FAILURE.getValue());
                subOrderInfoDto.setMessage(subOrderInfoDto.getMessage() + "\n ,  res_code = " + retCode);
                rewardOrderInfoDto.setOrder_status(OrderInfoType.FAILURE.getValue());
                rewardOrderInfoDto.setOrder_exception(rewardOrderInfoDto.getMessage() + ", suborder failure , sub_order_id = " + subOrderId);
            }

            validatorSubOrder(orderId, rewardOrderInfoDto,subOrderInfoDto,PHONE_CHARGE_SUCCESS);

            wrapResponseModel.setCode(successCode);

        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[pmi:phoneDirectCallback] failed", t);
        }
        return wrapResponseModel;
    }


    @POST
    @Path("/callback/fuel/pwd")
    @Produces(MediaType.APPLICATION_JSON)
    public FuelCardCallBackResModel fuleCardPwdCallback(FuelCardCallBackModel model){
        FuelCardCallBackResModel resModel = new FuelCardCallBackResModel();
        try {
            requireNonNull(model, "fuel card call back request is null");
            logger.info(String.format("[pmi:fuelcardpwd] body=" ));
            String subOrderId = model.getOrderNo();
            String orderStrs[] = subOrderId.split("_");
            if (orderStrs.length != 2) {
                throw new RuntimeException("sporder_id is not correct ,subOrderId =" + subOrderId);
            }
            String orderId = orderStrs[0];
            String batchNumber = orderStrs[1];
            //卡密
            String msg = "";
            //充值失败可能为空
            List<Card> cards = model.getResultDate();
            if (cards!=null){
                msg = JSONObject.toJSONString(cards);
            }
            //保存子订单表
            SubOrderInfoDto subOrderInfoDto = new SubOrderInfoDto();
            subOrderInfoDto.setSub_order_id(subOrderId);
            subOrderInfoDto.setOrder_id(orderId);
            subOrderInfoDto.setBatch_number(Integer.parseInt(batchNumber));
            subOrderInfoDto.setMessage(msg);
            subOrderInfoDto.setIndate(new Date());


            RewardOrderInfoDto rewardOrderInfoDto = (RewardOrderInfoDto) rewardOrderInfoDao.getObjectByID(orderId);
            requireNonNull(rewardOrderInfoDto, "parent order_id is not exist , order_id = " + orderId);

            //判断返回值
            String status = model.getOrderStatus();
            if (RECHAGE_OK==status){
                subOrderInfoDto.setState(OrderInfoType.SUCCESS.getValue());
            }else {
                subOrderInfoDto.setState(OrderInfoType.FAILURE.getValue());
                subOrderInfoDto.setMessage(subOrderInfoDto.getMessage() + "\n ,  status = " + status);
                rewardOrderInfoDto.setOrder_status(OrderInfoType.FAILURE.getValue());
                rewardOrderInfoDto.setOrder_exception(rewardOrderInfoDto.getMessage() + ", suborder failure , sub_order_id = " + subOrderId);
            }
            //回调
            validatorSubOrder(orderId, rewardOrderInfoDto, subOrderInfoDto,msg);

        }catch (Throwable t){
            resModel.setResult(ERROR);
            resModel.setResult_desc(ExceptionUtils.getMessage(t));
        }

        resModel.setResult(SUCCESS);
        return resModel;
    }
}

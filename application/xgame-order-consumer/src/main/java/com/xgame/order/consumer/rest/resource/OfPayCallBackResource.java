package com.xgame.order.consumer.rest.resource;

import com.xgame.service.common.rest.model.WrapResponseModel;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Created by william on 2017/9/26.
 */
@Path("/ofpay")
public class OfPayCallBackResource extends BaseResources{

    private static Logger logger = LoggerFactory.getLogger(OfPayCallBackResource.class.getName());


    /**
     * 电话卡直冲，回调
     *
     * 系统请求参数：ret_code 充值后状态，1代表成功，9代表撤消
     *
     * <p>
     * <p>
     * ：ret_code=1&sporder_id=test001234567&ordersuccesstime=20160817140214&err_msg=
     *
     *
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
//            AdvConfigDto advConfigDto = parseAdvConfigModelToDto(advConfigModel);
//            advConfigDto.setLast_edit_date(new Date());
//            advConfigDto.setLast_edit_user_id(getUid());
//            advConfigService.saveObject(advConfigDto);
//            wrapResponseModel.setData(parseAdvConfigDtoToModel(advConfigDto));
            wrapResponseModel.setCode(successCode);
        } catch (Throwable t) {
            wrapResponseModel.setCode(errorCode);
            wrapResponseModel.setDebug(ExceptionUtils.getStackTrace(t));
            logger.error("[ofpay:phoneDirectCallback] failed", t);
        }
        return wrapResponseModel;
    }
}

package com.xgame.service.manager.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.rest.model.kpi.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on 2017/9/11.
 */
@Path("/kpi")
public class KpiResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(KpiResources.class.getName());

    /**
     *
     * 日活
     *
     * @return
     */
    @GET
    @Path("/getActive")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getActive() {
        logger.info("[kpi:getActive]" + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            List<ActiveModel> activeModels = kpiDao.getActive();
            wrapResponseModel.setData(activeModels);
            wrapResponseModel.setCode(successCode);
        }catch (Throwable t){
            wrapResponseModel.setCode(errorCode);
            logger.error("[kpi:getActive] error "+ ExceptionUtils.getStackTrace(t));
        }
        return wrapResponseModel;
    }

    /**
     * 新用户
     *
     * @return
     */
    @GET
    @Path("/getNewActive")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getNewActive() {
        logger.info("[kpi:getNewActive]" + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            List<NewActiveModel> newActiveModels = kpiDao.getNewActive();
            wrapResponseModel.setData(newActiveModels);
            wrapResponseModel.setCode(successCode);
        }catch (Throwable t){
            wrapResponseModel.setCode(errorCode);
            logger.error("[kpi:getNewActive] error "+ ExceptionUtils.getStackTrace(t));
        }
        return wrapResponseModel;
    }


    /**
     * 支付金额
     *
     * @return
     */
    @GET
    @Path("/getPay")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getPay() {
        logger.info("[kpi:getPay]" + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            List<PayModel> payModels = kpiDao.getPay();
            wrapResponseModel.setData(payModels);
            wrapResponseModel.setCode(successCode);
        }catch (Throwable t){
            wrapResponseModel.setCode(errorCode);
            logger.error("[kpi:getPay] error "+ ExceptionUtils.getStackTrace(t));
        }
        return wrapResponseModel;
    }


    /**
     * 支付金额
     *
     * @return
     */
    @GET
    @Path("/getPayNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getPayNumber() {
        logger.info("[kpi:getPayNumber]" + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            List<PayModelNumber> payModelNumbers = kpiDao.getPayNumber();
            wrapResponseModel.setData(payModelNumbers);
            wrapResponseModel.setCode(successCode);
        }catch (Throwable t){
            wrapResponseModel.setCode(errorCode);
            logger.error("[kpi:getPayNumber] error "+ ExceptionUtils.getStackTrace(t));
        }
        return wrapResponseModel;
    }


    /**
     * 支付金额
     *
     * @return
     */
    @GET
    @Path("/getRetention")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getRetention() {
        logger.info("[kpi:getRetention]" + getUid());
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        try {
            List<RetentionModel> retentionModels = kpiDao.getRetention();
            wrapResponseModel.setData(retentionModels);
            wrapResponseModel.setCode(successCode);
        }catch (Throwable t){
            wrapResponseModel.setCode(errorCode);
            logger.error("[kpi:getRetention] error "+ ExceptionUtils.getStackTrace(t));
        }
        return wrapResponseModel;
    }


    @GET
    @Path("/getNav")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getKpiNav() {
        logger.info("getKpiNav");

        List<KpiNavModel> kpiNavModels = new ArrayList<>();

        List<KpiMetaModel> kpiMetaModelList1 = new ArrayList<>();
        kpiMetaModelList1.add(new KpiMetaModel(1,"在线人数"));
        kpiMetaModelList1.add(new KpiMetaModel(2,"离线人数"));
        KpiNavModel kpiNavModel = new KpiNavModel();
        kpiNavModel.setKpiMetaModelList(kpiMetaModelList1);
        kpiNavModel.setNavName("人数统计");
        kpiNavModels.add(kpiNavModel);

        List<KpiMetaModel> kpiMetaModelList2 = new ArrayList<>();
        kpiMetaModelList2.add(new KpiMetaModel(3,"日活"));
        kpiMetaModelList2.add(new KpiMetaModel(4,"月活"));
        KpiNavModel kpiNavModel2 = new KpiNavModel();
        kpiNavModel2.setKpiMetaModelList(kpiMetaModelList2);
        kpiNavModel2.setNavName("活跃统计");
        kpiNavModels.add(kpiNavModel2);

        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setData(kpiNavModels);
        wrapResponseModel.setCode(successCode);
        return wrapResponseModel;
    }

    @GET
    @Path("/getData")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getKpiData(@QueryParam("id") String kpiId){
        logger.info("getKpiData,kipid:"+kpiId);
        if(StringUtils.isEmpty(kpiId)){
            throw new IllegalArgumentException("navId is empty");
        }
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        KpiDataModel kpiDataModel = new KpiDataModel();
        List<String> columns = new ArrayList<>();
        columns.add("在线人数");
        columns.add("离线人数");

        List<Object> result = new ArrayList<>();
        List<Object> dataList1 = new ArrayList<>();
        dataList1.add(100);
        dataList1.add(200);
       // activeMans.add(200);
        result.add(dataList1);
        List<Object> dataList2 = new ArrayList<>();
        dataList2.add(300);
        dataList2.add(400);
        result.add(dataList2);

        List<Object> columObjects = new ArrayList<>();
        columObjects.add(result);



        kpiDataModel.setColumns(columns);
        kpiDataModel.setData(columObjects);
        wrapResponseModel.setData(kpiDataModel);
        wrapResponseModel.setCode(successCode);
        return wrapResponseModel;

    }
}

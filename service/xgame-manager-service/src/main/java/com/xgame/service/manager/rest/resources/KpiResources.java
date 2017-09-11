package com.xgame.service.manager.rest.resources;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.rest.model.kpi.KpiDataModel;
import com.xgame.service.manager.rest.model.kpi.KpiMetaModel;
import com.xgame.service.manager.rest.model.kpi.KpiNavModel;
import org.apache.commons.lang3.StringUtils;
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
        List<Object> objects = new ArrayList<>();
        objects.add(100);
        kpiDataModel.setColumns(columns);
        kpiDataModel.setData(objects);
        wrapResponseModel.setData(kpiDataModel);
        wrapResponseModel.setCode(successCode);
        return wrapResponseModel;

    }
}

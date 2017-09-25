package com.xgame.service.game.receive.rest.resources;

import com.xgame.service.common.conf.ConfigHolder;
import com.xgame.service.common.conf.OrderInfo;
import com.xgame.service.game.receive.rest.model.recall.FuelModel;
import com.xgame.service.game.receive.rest.model.recall.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import java.io.UnsupportedEncodingException;

@Path("/recall")
public class RecallResources {
    private static Logger logger = LoggerFactory.getLogger(RecallResources.class.getName());
    @POST
    @Path("/fuel")
    @Produces(MediaType.APPLICATION_JSON)
    public ResultModel fuelRecall(String model){
        logger.info("fuel recall:"+model);
        ResultModel resultModel = new ResultModel();
        resultModel.setResult("SUCCESS");
        resultModel.setResult_desc("收到结果");
        return resultModel;
    }

    @POST
    @Path("/phone")
    @Produces(MediaType.APPLICATION_XML)
    public String phoneRecall(String xml){
        ResultModel resultModel = new ResultModel();
        try {
            logger.info("phone recall:"+xml);
            OrderInfo orderInfo= ConfigHolder.getPhoneChargeXml(xml);
            System.out.println(orderInfo.getReqcode());
            resultModel.setResult("SUCCESS");
            resultModel.setResult_desc("收到结果");
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "success";
    }
}

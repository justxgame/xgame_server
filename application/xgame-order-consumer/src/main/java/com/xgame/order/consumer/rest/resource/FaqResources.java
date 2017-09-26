package com.xgame.order.consumer.rest.resource;

import com.xgame.service.common.rest.model.WrapResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by william on 2017/9/11.
 */

@Path("/faq")
public class FaqResources extends BaseResources{
    private static Logger logger = LoggerFactory.getLogger(FaqResources.class.getName());


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getAll() {
        logger.info("faq invoke ok 11");
        WrapResponseModel wrapResponseModel = new WrapResponseModel();
        wrapResponseModel.setCode(0);
        return wrapResponseModel;
    }
}

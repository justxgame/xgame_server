package com.xgame.order.consumer.conf;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class Configuration
{
    private static Logger logger =  LoggerFactory.getLogger(Configuration.class.getName());

    private final String SERVICE_CONF_NAME = "xgame-orde-consumer.properties";

    private org.apache.commons.configuration.Configuration config;

    private static class ServiceConfigurationHandler {
        public static Configuration serviceConfiguration = new Configuration();
    }

    public static Configuration getInstance() {
        return ServiceConfigurationHandler.serviceConfiguration;
    }

    public Configuration() {
        try {
            config = new PropertiesConfiguration(SERVICE_CONF_NAME);
        } catch (ConfigurationException e) {
            logger.error("[Configuration] init error ,exist system!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public org.apache.commons.configuration.Configuration getConfig() {
        return config;
    }
}
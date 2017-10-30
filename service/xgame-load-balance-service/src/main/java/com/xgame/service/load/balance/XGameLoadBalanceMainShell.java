package com.xgame.service.load.balance;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.IOException;
import java.net.URL;


/**
 * Created by william on 2017/9/9.
 */
public class XGameLoadBalanceMainShell {

    public static void main(String[] args) {

        try {
            System.out.println("start web server ...");
            final String appDir = new XGameLoadBalanceMainShell().getWebAppsPath();
            final Server server = new Server(ServiceConfiguration.getInstance().getConfig().getInt("xgame.load.balance.service.port", 9105));
            WebAppContext context = new WebAppContext();
            context.setContextPath("/");
            context.setBaseResource(Resource.newResource(appDir));
            server.setHandler(context);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("webapp not found in CLASSPATH.");
            System.exit(2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("start metastore service error.");
            System.exit(1);
        }
    }

    private String getWebAppsPath() throws IOException {
        URL url = getClass().getClassLoader().getResource("webapps");
        if (url == null)
            throw new IOException("webapp not found in CLASSPATH");
        return url.toString();
    }



}

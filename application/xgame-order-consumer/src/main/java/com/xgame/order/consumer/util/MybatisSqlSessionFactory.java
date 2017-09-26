//package com.xgame.order.consumer.util;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.util.Properties;
//
//import org.apache.ibatis.datasource.DataSourceFactory;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class MybatisSqlSessionFactory {
//    private static Logger logger = LoggerFactory.getLogger(MybatisSqlSessionFactory.class.getName());
//    private static SqlSessionFactory sqlSessionFactory;
//
//    private static final Properties PROPERTIES = new Properties();
//
//    static
//    {
//        try {
//            InputStream is = DataSourceFactory.class.getResourceAsStream("/db.properties");
//            PROPERTIES.load(is);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }
//
//    public static SqlSessionFactory getSqlSessionFactory()
//    {
//        if(sqlSessionFactory==null)
//        {
//            InputStream inputStream = null;
//            try
//            {
//                inputStream = Resources.getResourceAsStream("mybatis.xml");
//                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//            }catch (IOException e)
//            {
//                throw new RuntimeException(e.getCause());
//            }finally {
//                if(inputStream != null){
//                    try {
//                        inputStream.close();
//                    } catch (IOException e) {
//                    }
//                }
//            }
//        }
//        return sqlSessionFactory;
//    }
//
//    public static SqlSession getSqlSession()
//    {
//        return getSqlSessionFactory().openSession();
//    }
//    public static SqlSession getSqlSessionAutoCommit(){
//        return getSqlSessionFactory().openSession(true);
//    }
//
//
//    public static Connection getConnection()
//    {
//        String driver = PROPERTIES.getProperty("jdbc.driverClassName");
//        String url = PROPERTIES.getProperty("jdbc.url");
//        String username = PROPERTIES.getProperty("jdbc.username");
//        String password = PROPERTIES.getProperty("jdbc.password");
//        Connection connection = null;
//        try {
//            Class.forName(driver);
//            connection = DriverManager.getConnection(url, username, password);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return connection;
//    }
//}

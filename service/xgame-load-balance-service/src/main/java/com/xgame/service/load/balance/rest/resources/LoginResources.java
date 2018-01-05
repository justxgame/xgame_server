package com.xgame.service.load.balance.rest.resources;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcloud.msg.http.HttpSender;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.load.balance.ServiceConfiguration;
import com.xgame.service.load.balance.db.dto.PhoneCodeDto;
import com.xgame.service.load.balance.db.dto.UserBasicDto;
import com.xgame.service.load.balance.db.dto.UserLoginDto;
import com.xgame.service.load.balance.rest.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Path("/userLogin")
public class LoginResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(LoginResources.class.getName());
    private static final Integer PHONE_INTERVAL = ServiceConfiguration.getInstance().getConfig().getInt("xgame.phone.interval");
    private static final String SMS_URI=ServiceConfiguration.getInstance().getConfig().getString("xgame.sms.uri");
    private static final String SMS_USER=ServiceConfiguration.getInstance().getConfig().getString("xgame.sms.user");
    private static final String SMS_PWD= ServiceConfiguration.getInstance().getConfig().getString("xgame.sms.pwd");
    private static final String SMS_CONTENT = "验证码%s（15分钟内有效，如非本人操作请忽略.)";
    private static final Integer LOGIN = 0;
    private static final Integer REGISTER = 1;
    private static final String XY_URL = ServiceConfiguration.getInstance().getConfig().getString("xgame.xy.url");
    private static final String APPID = ServiceConfiguration.getInstance().getConfig().getString("xgame.xy.appid");
    private static final String XY_APP_KEY = ServiceConfiguration.getInstance().getConfig().getString("xgame.xy.appkey");
    private static final String WAN_URL = ServiceConfiguration.getInstance().getConfig().getString("xgame.9wan.url");
    @GET
    @Path("/getSmsCode")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel getSmsCode(@QueryParam("phone")String phone,@QueryParam("type") String type){
        WrapResponseModel responseModel = new WrapResponseModel();


        logger.info("[LoginResources] getSmsCode. phone= "+phone+" type="+type);
        try {
            requireNonNull(phone, "[LoginResources] get null phone");
            PhoneCodeDto dto = userLoginService.getPhone(phone);
            if (null!=dto){
                String inDate = dto.getIn_date();
                long intime =CommonUtil.parseStr2Time(inDate);
                if(!isValidSendCode(intime,PHONE_INTERVAL)){
                    throw new RuntimeException("please after " + PHONE_INTERVAL + " minute send code");
                }
            }

            String code = getVerificationCode();
            //send code
            sendSms(phone,code,SMS_USER,SMS_PWD,SMS_URI);
            String date = CommonUtil.getFormatDateByNow();
            userLoginService.saveCode(phone,code,date);
            responseModel.setCode(successCode);
            responseModel.setData(code);


        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[LoginResources]  getSmsCode  error "+ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }

    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel login(@QueryParam("account") String account, @QueryParam("code") String code,
                                   @QueryParam("type") Integer type, @QueryParam("inviter_uname") String inviter_uname,
                                   @QueryParam("login_type")Integer loginType,@QueryParam("inviter_type")Integer inviteType) {
        WrapResponseModel responseModel = new WrapResponseModel();
        logger.info("[LoginResources] login.account="+account+" code="+code+" type="+type+" inviter_uname="+
                inviter_uname+" loginType="+loginType+" inviter_type="+inviteType);
        if (!isValidLoginParam(account,code,type,loginType)){
            responseModel.setCode(errorCode);
            responseModel.setMsg("error login param");
        }
        try {
            String phone = "";

            UserLoginDto userLoginDto = userLoginService.getLogInfoByUname(account);
            String token = generateSessionToken(account);
            String indate = CommonUtil.getFormatDateByNow();
            UserLoginDto dto = new UserLoginDto();
            dto.setUname(account);
            dto.setSession_token(token);
            dto.setType(type);
            dto.setInviter_uname(inviter_uname);
            dto.setIn_date(indate);
            dto.setInviter_type(inviteType);
            //手机登陆
            if (type==0){
                phone=account;
                //注册验证是否注册过
                if (loginType.equals(REGISTER)){


                    if (null!=userLoginDto){
                        responseModel.setCode(errorCode);
                        responseModel.setMsg("手机已被注册,请更换号码");
                        return responseModel;
                    }
                }
                //验证成功
                if (isValidPhoneCode(phone,code)){


                    //注册
                    if (loginType.equals(REGISTER)){

                        userLoginService.saveUserLoginInfo(dto);
                    }else {
                        //登陆
                        //如果没有注册则注册否则只更改状态
                        if(null==userLoginDto){
                            userLoginService.saveUserLoginInfo(dto);
                        }else {
                            userLoginDto = userLoginService.getLogInfoByUname(account);
                            userLoginService.updateLoginToken(userLoginDto.getAccount_id(),token);

                        }

                    }
                    //获取 account id
                    userLoginDto = userLoginService.getLogInfoByUname(account);

                    LoginUser loginUser = new LoginUser();
                    loginUser.setAccountid(userLoginDto.getAccount_id());
                    loginUser.setUname(account);
                    UserLogInInfo userLogInInfo = new UserLogInInfo();
                    userLogInInfo.setU_info(loginUser);
                    userLogInInfo.setSession_token(token);
                    responseModel.setData(userLogInInfo);
                    responseModel.setCode(successCode);
                }else {

                    responseModel.setCode(errorCode);
                    responseModel.setMsg("invalid code");
                    return responseModel;
                }
            }else if (5==type){
                //xy 验证

                HttpEntity entity =null;
                CloseableHttpResponse response=null;
                try {

                    String sign = getSign(APPID, account, code,XY_APP_KEY);
                    HttpPost httpPost = new HttpPost(XY_URL);
                    httpPost.setHeader("Content-type","application/x-www-form-urlencoded");
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("appid", APPID));
                    params.add(new BasicNameValuePair("uid", account));
                    params.add(new BasicNameValuePair("token", code));
                    params.add(new BasicNameValuePair("sign", sign));
                    logger.info("[LoginResources] request params = " + getParameterStr(params));
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    logger.info("[LoginResources] request=" + httpPost.getParams());
                    response = httpclient.execute(httpPost);
                    entity = response.getEntity();
                    String res = EntityUtils.toString(entity);
                    XyResponse xyResponse = JSONObject.parseObject(res, XyResponse.class);
                    if (null==xyResponse||!xyResponse.getCode().equals("200")){
                        responseModel.setCode(404);
                        responseModel.setMsg("登陆失败");
                        responseModel.setMessage(xyResponse.getMsg());
                        return responseModel;
                    }else {
                        //其他方式登录且没注册
                        if(null==userLoginDto){
                            userLoginService.saveUserLoginInfo(dto);
                        }else {
                            userLoginDto = userLoginService.getLogInfoByUname(account);
                            userLoginService.updateLoginToken(userLoginDto.getAccount_id(),token);

                        }
                        //获取 account id
                        userLoginDto = userLoginService.getLogInfoByUname(account);

                        LoginUser loginUser = new LoginUser();
                        loginUser.setAccountid(userLoginDto.getAccount_id());
                        loginUser.setUname(account);
                        UserLogInInfo userLogInInfo = new UserLogInInfo();
                        userLogInInfo.setU_info(loginUser);
                        userLogInInfo.setSession_token(token);
                        responseModel.setData(userLogInInfo);
                        responseModel.setCode(successCode);
                    }
                }catch (Throwable t){
                    responseModel.setCode(404);
                    responseModel.setMessage(ExceptionUtils.getMessage(t));
                    responseModel.setMsg("登陆失败");
                    logger.error("[LoginResources]  login  error. "+ExceptionUtils.getMessage(t));
                    return responseModel;
                }finally {
                    try {
                        if (response!=null){
                            response.close();
                        }
                        EntityUtils.consume(entity);

                    } catch (IOException e) {
                        logger.error("[LoginResources]  login  error");
                    }
                }
            }else if (4==type){
                HttpEntity entity =null;
                CloseableHttpResponse response=null;
                try {
                    HttpPost httpPost = new HttpPost(WAN_URL);
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("user_id", account));
                    params.add(new BasicNameValuePair("token", code));
                    logger.info("[LoginResources] request params = " + getParameterStr(params));
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    logger.info("[LoginResources] request=" + httpPost.getParams());
                    response = httpclient.execute(httpPost);
                    entity = response.getEntity();
                    String res = CommonUtil.unicodeToString(EntityUtils.toString(entity));
                    WanResponse wanResponse = JSONObject.parseObject(res,WanResponse.class);
                    if (null==wanResponse||1!=wanResponse.getStatus()){
                        responseModel.setCode(404);
                        responseModel.setMsg("登陆失败");
                        responseModel.setMessage(wanResponse.getMsg());
                        return responseModel;
                    }else {
                        if(null==userLoginDto){
                            userLoginService.saveUserLoginInfo(dto);
                        }else {
                            userLoginDto = userLoginService.getLogInfoByUname(account);
                            userLoginService.updateLoginToken(userLoginDto.getAccount_id(),token);

                        }
                        //获取 account id
                        userLoginDto = userLoginService.getLogInfoByUname(account);

                        LoginUser loginUser = new LoginUser();
                        loginUser.setAccountid(userLoginDto.getAccount_id());
                        loginUser.setUname(account);
                        UserLogInInfo userLogInInfo = new UserLogInInfo();
                        userLogInInfo.setU_info(loginUser);
                        userLogInInfo.setSession_token(token);
                        responseModel.setData(userLogInInfo);
                        responseModel.setCode(successCode);
                    }

                }catch (Throwable t){
                    responseModel.setCode(404);
                    responseModel.setMessage(ExceptionUtils.getMessage(t));
                    responseModel.setMsg("登陆失败");
                    logger.error("[LoginResources]  login  error. "+ExceptionUtils.getMessage(t));
                    return responseModel;
                }finally {
                    try {
                        if (response!=null){
                            response.close();
                        }
                        EntityUtils.consume(entity);

                    } catch (IOException e) {
                        logger.error("[LoginResources]  login  error");
                    }
                }
            }
            else {
                //其他方式登录且没注册
                if(null==userLoginDto){
                    userLoginService.saveUserLoginInfo(dto);
                }else {
                    userLoginDto = userLoginService.getLogInfoByUname(account);
                    userLoginService.updateLoginToken(userLoginDto.getAccount_id(),token);

                }
                //获取 account id
                userLoginDto = userLoginService.getLogInfoByUname(account);

                LoginUser loginUser = new LoginUser();
                loginUser.setAccountid(userLoginDto.getAccount_id());
                loginUser.setUname(account);
                UserLogInInfo userLogInInfo = new UserLogInInfo();
                userLogInInfo.setU_info(loginUser);
                userLogInInfo.setSession_token(token);
                responseModel.setData(userLogInInfo);
                responseModel.setCode(successCode);
            }


        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[LoginResources]  login  error "+ExceptionUtils.getMessage(t));
        }

        return responseModel;
    }


    @GET
    @Path("/autologin")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel autoLogin(@QueryParam("session_token")String sessionToken,@QueryParam("accountid")String accountId){
        WrapResponseModel responseModel = new WrapResponseModel();
        logger.info("[LoginResources] autologin. session_token="+sessionToken+" accountid="+accountId);
        try {
            requireNonNull(sessionToken, "empty session_token");
            requireNonNull(accountId, "empty accountId");
            UserLoginDto dto = userLoginService.getLoginInfo(accountId, sessionToken);
            requireNonNull(dto, "get null login info by token and accountid,token is invalid");

            String indate = CommonUtil.getFormatDateByNow();
            LoginUser loginUser = new LoginUser();
            loginUser.setAccountid(accountId);
            loginUser.setUname(dto.getUname());
            UserLogInInfo logInInfo = new UserLogInInfo();
            String newToken = generateSessionToken(accountId);
            logInInfo.setSession_token(newToken);
            logInInfo.setU_info(loginUser);
            userLoginService.updateLoginToken(accountId,newToken);

            responseModel.setCode(successCode);
            responseModel.setData(logInInfo);

        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[LoginResources]  autologin  error "+ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }

    @GET
    @Path("/checkregister")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel checkPhone(@QueryParam("phone")String phone){
        WrapResponseModel responseModel = new WrapResponseModel();
        logger.info("[LoginResources] checkregister. phone="+phone);
        try {
            requireNonNull(phone, "phone is empty");
            UserLoginDto dto = userLoginService.getLogInfoByUname(phone);
            CheckPhoneInfo checkPhoneInfo = new CheckPhoneInfo();
            if (null==dto){
                checkPhoneInfo.setResult(1);

            }else {
                checkPhoneInfo.setResult(0);
            }
            responseModel.setData(checkPhoneInfo);
            responseModel.setCode(successCode);

        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[LoginResources]  checkregister  error "+ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }


    @GET
    @Path("/checkid")
    @Produces(MediaType.APPLICATION_JSON)
    public WrapResponseModel checkId(@QueryParam("session_token")String sessionToken,@QueryParam("accountid")String accountId){
        WrapResponseModel responseModel = new WrapResponseModel();
        logger.info("[LoginResources] checkid. session_token="+sessionToken+" accountid="+accountId);
        try {
            requireNonNull(sessionToken, "empty session_token");
            requireNonNull(accountId, "empty accountId");
            UserLoginDto dto = userLoginService.getLoginInfo(accountId, sessionToken);
            requireNonNull(dto, "get null login info by token and accountid,token is invalid");

            UserCheckInfo userCheckInfo = new UserCheckInfo();
            userCheckInfo.setInviter_uname(dto.getInviter_uname());
            userCheckInfo.setMsg("登陆成功");
            userCheckInfo.setTime(System.currentTimeMillis());
            userCheckInfo.setType(dto.getType());
            userCheckInfo.setUname(dto.getUname());

            responseModel.setCode(successCode);
            responseModel.setData(userCheckInfo);

        }catch (Throwable t){
            responseModel.setCode(errorCode);
            responseModel.setMessage(ExceptionUtils.getMessage(t));
            logger.error("[LoginResources]  checkid  error "+ExceptionUtils.getMessage(t));
        }
        return responseModel;
    }


    public String getVerificationCode(){
        int code = (int)(Math.random() * 9000 + 1000);
        return String.valueOf(code);
    }

    private boolean isValidPhoneCode(String phone,String code){
        PhoneCodeDto phoneCodeDto =userLoginService.getPhone(phone);
        if (null==phoneCodeDto){
            return false;
        }
        if (phoneCodeDto.getCode().equals(code)){
            return true;

        }
        return false;
    }
    private String getSign(String appid, String account, String code,String appkey) {
        String unionStr = appkey+"appid="+appid+"&"+"token="+code+"&"+"uid="+account;
        String sign = Hashing.md5().hashString(unionStr, Charsets.UTF_8).toString();
        return sign;
    }


    /**
     * 判断发送 code 请求是否在指定间隔时间外
     * @param time
     * @param interval
     * @return
     */
    private boolean isValidSendCode(long time,int interval){
        long now = System.currentTimeMillis();
        long timeInterval = interval*60000;
        if ((now-time)<timeInterval){
            return false;
        }
        return true;
    }

    private void sendSms(String phone,String code,String user,String pwd,String uri) throws Exception {
        boolean needstatus = true;
        String content=String.format(SMS_CONTENT,code);
        String product = "";//产品ID(不用填写)
        String extno = "";//扩展码(请登陆网站用户中心——>服务管理找到签名对应的extno并填写，线下用户请为空)
        String returnString = HttpSender.batchSend(uri, user, pwd, phone, content, needstatus, product, extno);

    }




    private boolean isValidLoginParam(String account,String code,Integer type,Integer loginType){
        if(StringUtils.isEmpty(account)||StringUtils.isEmpty(code)||null==type||null==loginType){
            return false;
        }
        return true;
    }

    private String generateAccountId(String account,Integer type){
        return account+"_"+type;
    }

    private String generateSessionToken(String accountId){
        return accountId+"_"+System.currentTimeMillis();
    }
    protected String getParameterStr(List<NameValuePair> params) {
        if (null == params) {
            return "";
        }
        String paramsStr = "";
        for (NameValuePair pair : params) {
            if (null != pair) {
                paramsStr = paramsStr +","+ pair.toString();
            }
        }
        return paramsStr;
    }



}

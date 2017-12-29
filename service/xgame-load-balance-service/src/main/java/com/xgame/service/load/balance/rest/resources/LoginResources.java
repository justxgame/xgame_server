package com.xgame.service.load.balance.rest.resources;


import com.bcloud.msg.http.HttpSender;
import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.common.util.CommonUtil;
import com.xgame.service.load.balance.ServiceConfiguration;
import com.xgame.service.load.balance.db.dto.PhoneCodeDto;
import com.xgame.service.load.balance.db.dto.UserBasicDto;
import com.xgame.service.load.balance.db.dto.UserLoginDto;
import com.xgame.service.load.balance.rest.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static java.util.Objects.requireNonNull;

@Path("/userLogin")
public class LoginResources extends BaseResources {
    private static Logger logger = LoggerFactory.getLogger(LoginResources.class.getName());
    private static final Integer PHONE_INTERVAL = ServiceConfiguration.getInstance().getConfig().getInt("xgame.phone.interval");
    private static final String SMS_URI=ServiceConfiguration.getInstance().getConfig().getString("xgame.sms.uri");
    private static final String SMS_USER=ServiceConfiguration.getInstance().getConfig().getString("xgame.sms.user");
    private static final String SMS_PWD= ServiceConfiguration.getInstance().getConfig().getString("xgame.sms.pwd");
    private static final String SMS_CONTENT = "验证码%s（15分钟内有效，如非本人操作请忽略.)";
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
                                   @QueryParam("type") Integer type, @QueryParam("inviter_uname") String inviter_uname) {
        WrapResponseModel responseModel = new WrapResponseModel();
        logger.info("[LoginResources] login.account="+account+" code="+code+" type="+type+" inviter_uname="+inviter_uname);
        if (!isValidLoginParam(account,code,type)){
            responseModel.setCode(errorCode);
            responseModel.setMsg("error login param");
        }
        try {
            String phone = "";
            //手机登陆
            if (type==0){
                phone=account;
            }else {
                UserBasicDto userBasicDto = userService.getUserBasic(account);
                if (null==userBasicDto){
                    throw new RuntimeException("get null userInfo by account");
                }
                phone = userService.getUserPhone(userBasicDto.getUid().toString(), userBasicDto.getServer_id().toString());
                if (StringUtils.isEmpty(phone)) {
                    throw new RuntimeException("get empty phone by account");
                }
            }
            if (isValidPhoneCode(phone,code)){
                String accountId = generateAccountId(account,type);
                String token = generateSessionToken(accountId);
                String indate = CommonUtil.getFormatDateByNow();
                UserLoginDto dto = new UserLoginDto();

                dto.setAccount(account);
                dto.setUname(account);
                dto.setAccount_id(accountId);
                dto.setSession_token(token);
                dto.setType(type);
                dto.setInviter_uname(inviter_uname);
                dto.setIn_date(indate);
                userLoginService.saveUserLoginInfo(dto);

                LoginUser loginUser = new LoginUser();
                loginUser.setAccountid(accountId);
                loginUser.setUname(account);
                UserLogInInfo userLogInInfo = new UserLogInInfo();
                userLogInInfo.setU_info(loginUser);
                userLogInInfo.setSession_token(token);
                responseModel.setData(userLogInInfo);
                responseModel.setCode(successCode);
            }else {

                responseModel.setCode(errorCode);
                responseModel.setMsg("invalid phone code");
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
            PhoneCodeDto dto = userLoginService.getPhone(phone);
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




    private boolean isValidLoginParam(String account,String code,Integer type){
        if(StringUtils.isEmpty(account)||StringUtils.isEmpty(code)||null==type){
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


}

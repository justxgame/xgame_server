package com.xgame.service.manager.rest.resources.auth;

import com.xgame.service.common.rest.model.WrapResponseModel;
import com.xgame.service.manager.ServiceConfiguration;
import com.xgame.service.manager.ServiceContextFactory;
import com.xgame.service.manager.db.dto.TokenDto;
import com.xgame.service.manager.db.dto.UserDto;
import com.xgame.service.manager.service.TokenService;
import com.xgame.service.manager.service.UserService;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class AuthorizationFilter implements ContainerRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class.getName());
    private TokenService tokenService = ServiceContextFactory.tokenService;
    private UserService userService = ServiceContextFactory.userService;

    final boolean isAuthOpen = ServiceConfiguration.getInstance().getConfig().getBoolean("xgame.manager.auth.open", true);

    /**
     * 权限验证错误
     */
    protected final int auth_access_failed = -1000;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        //如果是登录或者注册，不检测
        UriInfo info = requestContext.getUriInfo();
        if (info.getPath().contains("login")||info.getPath().contains("register")) {
            return;
        }

        //获取客户端Header中提交的token
        String token = requestContext.getHeaderString("token");
        if (StringUtil.isBlank(token)) {
            // 如果不打开权限认证， 默认以匿名账号登录
            if (!isAuthOpen) {
                logger.info("[AuthorizationFilter] login as anonymous");
                requestContext.getHeaders().add("uid", "anonymous");
            }else{
                logger.info("[AuthorizationFilter] token is empty");
                WrapResponseModel wrapResponseModel = new WrapResponseModel();
                wrapResponseModel.setCode(auth_access_failed);
                wrapResponseModel.setMessage("token is empty");
                requestContext.abortWith(Response.ok(wrapResponseModel).build());
            }
        } else {
            TokenDto tokenDto = tokenService.getObjectByToken(token);
            if (null == tokenDto) {
                logger.info("[AuthorizationFilter] token is not correct");
                WrapResponseModel wrapResponseModel = new WrapResponseModel();
                wrapResponseModel.setCode(auth_access_failed);
                wrapResponseModel.setMessage("token 错误");
                requestContext.abortWith(Response.ok(wrapResponseModel).build());
            } else {
                requestContext.getHeaders().add("uid", tokenDto.getUser_id());
                UserDto userDto = userService.getUserID(tokenDto.getUser_id());
                if(userDto.getApprove()==0){
                    logger.info("[AuthorizationFilter] user is not approve");
                    WrapResponseModel wrapResponseModel = new WrapResponseModel();
                    wrapResponseModel.setCode(auth_access_failed);
                    wrapResponseModel.setMessage("用户没有权限");
                    requestContext.abortWith(Response.ok(wrapResponseModel).build());
                }
            }
        }
    }

}
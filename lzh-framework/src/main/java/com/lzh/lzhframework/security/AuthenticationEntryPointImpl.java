package com.lzh.lzhframework.security;


import com.alibaba.fastjson.JSON;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {


        ResponseResult result = new ResponseResult();
        if (authException instanceof BadCredentialsException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(), authException.getMessage());
        } else if (authException instanceof InsufficientAuthenticationException) {
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        } else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "用户名或密码错误");
        }
//        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);

        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}

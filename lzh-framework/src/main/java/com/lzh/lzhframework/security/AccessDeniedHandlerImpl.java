package com.lzh.lzhframework.security;

import com.alibaba.fastjson.JSON;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LZH
 * @date 2023/2/16
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response, json);
    }
}

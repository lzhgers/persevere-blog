package com.lzh.lzhblog.interceptor;

import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.domain.entity.LoginUser;
import com.lzh.lzhblog.utils.RedisCache;
import com.lzh.lzhblog.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UpdateLoginTimeInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("执行了登陆更新拦截器拦截----------------");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return true;
        }
        Long userId;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            return true;
        }

        LoginUser loginUser = redisCache.getCacheObject(SysConstants.PRE_LOGIN_USER_REDIS + userId);
        if (Objects.isNull(loginUser)) {
            return true;
        }
        redisCache.expire(SysConstants.PRE_LOGIN_USER_REDIS + userId, 30, TimeUnit.MINUTES);
        return true;
    }
}

package com.lzh.lzhblog.interceptor;

import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class UpdateLoginTimeInterceptor implements HandlerInterceptor {

    @Resource
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("执行了登陆更新拦截器拦截----------------");

        String strUserId = request.getHeader("userId");
        Long userId = -1L;
        try {
            if (StringUtils.hasText(strUserId)) {
                userId = Long.valueOf(strUserId);
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            return true;
        }

        LoginUser loginUser = redisCache.getCacheObject(SysConstants.PRE_LOGIN_USER_REDIS + userId);
        if (Objects.isNull(loginUser)) {
            return true;
        }
        redisCache.expire(SysConstants.PRE_LOGIN_USER_REDIS + userId, 2, TimeUnit.HOURS);
        return true;
    }
}

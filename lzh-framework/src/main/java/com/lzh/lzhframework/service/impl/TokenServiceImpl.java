package com.lzh.lzhframework.service.impl;

import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.service.TokenService;
import com.lzh.lzhframework.service.UserService;
import com.lzh.lzhframework.utils.RedisCache;
import com.lzh.lzhframework.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 缓存实现类
 *
 * @Author luzhiheng
 * @Date 2023/9/22 11:34
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private UserService userService;

    @Override
    public boolean updateUserAvatar(String url) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        String key = SysConstants.SYS_USER_LOGIN + loginUser.getUser().getId();
        loginUser = redisCache.getCacheObject(key);
        if (loginUser == null || loginUser.getUser() == null) {
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        loginUser.getUser().setAvatar(url);
        // 更新缓存
        redisCache.setCacheObject(key, loginUser);

        // 更新数据库
        userService.updateUserAvatar(loginUser.getUser().getId(), url);
        return true;
    }
}

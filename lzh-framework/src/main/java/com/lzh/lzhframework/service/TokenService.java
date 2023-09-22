package com.lzh.lzhframework.service;

/**
 * 缓存接口
 * @Author luzhiheng
 * @Date 2023/9/22 11:34
 */
public interface TokenService {

    /**
     * 更新用户头像
     * @param url
     * @return
     */
    boolean updateUserAvatar(String url);
}

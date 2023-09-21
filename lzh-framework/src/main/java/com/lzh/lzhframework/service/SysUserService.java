package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Role;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.form.LoginForm;

import java.util.List;

/**
 * @author LZH
 * @date 2023/2/16
 */
public interface SysUserService {
    ResponseResult login(LoginForm loginForm);

    ResponseResult logout();

    ResponseResult getInfoByToken(String token);

    /**
     * 获取用户角色
     * @param userId
     * @return
     */
    List<Role> getUserRoles(Long userId);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    ResponseResult updateUserProfile(User user);

    /**
     * 查询用户个人信息
     * @return
     */
    ResponseResult avatar();

}

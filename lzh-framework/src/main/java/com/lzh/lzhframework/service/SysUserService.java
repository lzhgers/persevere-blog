package com.lzh.lzhframework.service;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.form.LoginForm;

/**
 * @author LZH
 * @date 2023/2/16
 */
public interface SysUserService {
    ResponseResult login(LoginForm loginForm);

    ResponseResult logout();
}

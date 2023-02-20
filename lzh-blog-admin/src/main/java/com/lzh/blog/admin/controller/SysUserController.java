package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.form.LoginForm;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author LZH
 * @date 2023/2/16
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginForm loginForm) {
        return sysUserService.login(loginForm);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return sysUserService.logout();
    }

    @GetMapping("/info")
    public ResponseResult info(String token) {
        return sysUserService.getInfoByToken(token);
    }


}

package com.lzh.blog.admin.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.LoginForm;
import com.lzh.lzhframework.service.SysUserService;
import com.lzh.lzhframework.utils.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @GetMapping("/avatar")
    public ResponseResult avatar() {
        return sysUserService.avatar();
    }

    @PutMapping("/updateUserProfile")
    public ResponseResult updateUserProfile(@RequestBody User user) {
        return sysUserService.updateUserProfile(user);
    }

    /**
     * 重置密码
     */
//    @PutMapping("/updatePwd")
//    public ResponseResult updatePwd(String oldPassword, String newPassword) {
//        User user = SecurityUtils.getLoginUser().getUser();
//
//        String password = user.getPassword();
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        if (!passwordEncoder.encode(oldPassword).equals(password)) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.OLD_PWD_ERROR);
//        }
//        if (passwordEncoder.encode(oldPassword).equals(password)) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.OLD_NEW_PWD_ALIKE);
//        }
//        if (userService.resetUserPwd(username, SecurityUtils.encryptPassword(newPassword)) > 0) {
//            // 更新缓存用户密码
//            LoginUser loginUser = SecurityUtils.getLoginUser();
//            loginUser.getSysUser().setPassword(SecurityUtils.encryptPassword(newPassword));
//            tokenService.setLoginUser(loginUser);
//            return AjaxResult.success();
//        }
//        return AjaxResult.error("修改密码异常，请联系管理员");
//    }

}

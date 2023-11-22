package com.lzh.lzhblog.controller;

import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.*;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.domain.vo.UserCommunicationVo;
import com.lzh.lzhframework.service.UserService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/email/getCode/{email}")
    public ResponseResult getCode(@PathVariable String email) {
        return userService.getEmailCode(email);
    }

    @PostMapping("/regist")
    public ResponseResult regist(@RequestBody UserDTO userDTO) {
        return userService.regist(userDTO);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return userService.logout();
    }

    /**
     * 根据文章id查询文章作者
     *
     * @param articleId 文章id
     * @return
     */
    @GetMapping("/getUserByArticleId")
    public ResponseResult getUserByArticleId(Long articleId) {
        User user = userService.getUserByArticleId(articleId);
        return ResponseResult.success(user);
    }

    @GetMapping("/getUserById")
    public ResponseResult getUserById(Long userId) {
        User user = userService.getUserById(userId);
        return ResponseResult.success(user);
    }

    @PostMapping("/avatar")
    public ResponseResult updateUserAvatar(Long userId, String avatar) {
        return userService.updateUserAvatar(userId, avatar);
    }

    @PutMapping("/updateUser")
    public ResponseResult updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        User user = BeanCopyUtils.copyBean(updateUserDTO, User.class);
        userService.updateById(user);
        return ResponseResult.success();
    }

    @PutMapping("/updatePassword")
    public ResponseResult updatePassword(@RequestBody UpdatePwdDTO updatePwdDTO) {
        return userService.updatePasswordByUserId(updatePwdDTO);
    }

    @PutMapping("/email/getUpdateEmailCode")
    public ResponseResult getUpdateEmailCode(@RequestBody UpdateEmailDTO updateEmailDTO) {
        return userService.getUpdateEmailCode(updateEmailDTO);
    }

    @PostMapping("/email/getNewEmailCode")
    public ResponseResult getNewEmailCode(@RequestBody UpdateEmailDTO updateEmailDTO) {
        return userService.getNewEmailCode(updateEmailDTO);
    }

    @PostMapping("/email/checkCode")
    public ResponseResult checkCode(@RequestBody UpdateEmailDTO updateEmailDTO) {
        return userService.checkCode(updateEmailDTO.getCode(), updateEmailDTO.getEmail());
    }

    @PostMapping("/email/finishEmailUpdate")
    public ResponseResult finishEmailUpdate(@RequestBody UpdateEmailDTO updateEmailDTO) {
        return userService.finishEmailUpdate(updateEmailDTO);
    }

    @PostMapping("/email/checkEmail")
    public ResponseResult checkEmail(@RequestBody UserDTO user) {
        return userService.checkEmail(user);
    }

    @PostMapping("/email/rePasswordCode")
    public ResponseResult getRePasswordCode(@RequestBody UserDTO user) {
        return userService.getRePasswordCode(user.getEmail());
    }

    @PutMapping("/rePassword")
    public ResponseResult rePassword(@RequestBody UpdatePwdDTO user) {
        return userService.rePassword(user.getEmail(), user.getNewPassword(), user.getConPassword());
    }

    @PostMapping("/email/checkRePwdCode")
    public ResponseResult checkRePwdCode(@RequestBody RePwdDTO user) {
        return userService.checkRePwdCode(user.getEmail(), user.getCode());
    }

    @DeleteMapping("/cancel/{userId}")
    public ResponseResult cancelAccount(@PathVariable Long userId) {
        return userService.cancelAccount(userId);
    }

    @PostMapping("/checkPassword")
    public ResponseResult checkPassword(@RequestBody UserDTO userDTO) {
        return userService.checkPassword(userDTO);
    }

    @PostMapping("/getCancelEmailCode")
    public ResponseResult getCancelEmailCode(@RequestBody UserDTO userDTO) {
        return userService.getCancelEmailCode(userDTO);
    }

    @PostMapping("/checkCancelEmailCode")
    public ResponseResult checkCancelEmailCode(@RequestBody UserDTO userDTO) {
        return userService.checkCancelEmailCode(userDTO);
    }

    @GetMapping("/isSubscribed/{userId}")
    public ResponseResult isSubscribed(@PathVariable Long userId) {
        Boolean res = userService.isSubscribed(userId);
        return ResponseResult.success(res);
    }

    @GetMapping("/getUserByUserName/{userName}")
    public ResponseResult getUserByUserName(@PathVariable String userName) {
        User user = userService.getUserByUserName(userName);

        UserCommunicationVo userCommunicationVo = BeanCopyUtils.copyBean(user, UserCommunicationVo.class);
        return ResponseResult.success(userCommunicationVo);
    }
}

package com.lzh.lzhframework.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.dto.UpdateEmailDTO;
import com.lzh.lzhframework.domain.dto.UpdatePwdDTO;
import com.lzh.lzhframework.domain.dto.UserDTO;
import com.lzh.lzhframework.domain.entity.User;

import java.util.List;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-09-26 16:50:18
 */
public interface UserService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult getEmailCode(String email);

    ResponseResult regist(UserDTO userDTO);

    ResponseResult logout();

    User getUserByArticleId(Long articleId);

    User getUserById(Long userId);

    ResponseResult updateUserAvatar(Long userId, String avatar);

    ResponseResult updatePasswordByUserId(UpdatePwdDTO updatePwdDTO);

    ResponseResult getUpdateEmailCode(UpdateEmailDTO updateEmailDTO);

    ResponseResult checkCode(String code, String email);

    ResponseResult getNewEmailCode(UpdateEmailDTO updateEmailDTO);

    ResponseResult finishEmailUpdate(UpdateEmailDTO updateEmailDTO);

    ResponseResult checkEmail(UserDTO user);

    ResponseResult getRePasswordCode(String email);

    ResponseResult rePassword(String email, String newPassword, String conPassword);

    ResponseResult checkRePwdCode(String email, String code);

    ResponseResult cancelAccount(Long userId);

    ResponseResult checkPassword(UserDTO userDTO);

    ResponseResult getCancelEmailCode(UserDTO userDTO);

    ResponseResult checkCancelEmailCode(UserDTO userDTO);

    Boolean isSubscribed(Long userId);

    User getUserByUserName(String userName);

    String getUserNameByUserId(Long createBy);

    List<User> listUserByName(String name);
}


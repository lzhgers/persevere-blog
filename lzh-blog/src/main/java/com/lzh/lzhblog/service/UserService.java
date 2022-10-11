package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.dto.UserDTO;
import com.lzh.lzhblog.domain.entity.User;


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
}

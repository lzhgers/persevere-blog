package com.lzh.lzhblog.controller;

import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.dto.UserDTO;
import com.lzh.lzhblog.domain.entity.User;
import com.lzh.lzhblog.service.UserService;
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
        return ResponseResult.okResult(user);
    }

    @GetMapping("/getUserById")
    public ResponseResult getUserById(Long userId) {
        User user = userService.getUserById(userId);
        return ResponseResult.okResult(user);
    }

    @PostMapping("/avatar")
    public ResponseResult updateUserAvatar(Long userId, String avatar) {
        return userService.updateUserAvatar(userId, avatar);
    }

}

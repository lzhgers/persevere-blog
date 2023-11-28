package com.lzh.lzhframework.utils;


import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.domain.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        try {
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            return null;
        }
    }

    public static Boolean isAdmin() {
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

    public static Long getUserId() {
        if (getLoginUser() == null) {
            return null;
        }
        User user = getLoginUser().getUser();
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    public static String getUsername() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null) {
            return loginUser.getUsername();
        }
        return "";
    }
}
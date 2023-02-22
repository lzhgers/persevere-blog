package com.lzh.lzhframework.service.impl;

import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.domain.vo.SysUserInfoVo;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.LoginForm;
import com.lzh.lzhframework.service.SysUserService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.JwtUtil;
import com.lzh.lzhframework.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author LZH
 * @date 2023/2/16
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private RedisCache redisCache;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult login(LoginForm loginForm) {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(loginForm.getUserName(), loginForm.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        if (Objects.isNull(authenticate)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        User user = loginUser.getUser();
        if (Objects.isNull(user)) {
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        Long userId = user.getId();

        redisCache.setCacheObject(SysConstants.SYS_USER_LOGIN + userId, loginUser);

        String jwt = JwtUtil.createJWT(userId.toString());



        Map<String, Object> map = new HashMap<>();
        map.put("token", jwt);

//        SysUserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, SysUserInfoVo.class);
//        map.put("userInfo", userInfoVo);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (Objects.isNull(loginUser)) {
            throw new SystemException(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        Long userId = loginUser.getUser().getId();
        redisCache.deleteObject(SysConstants.SYS_USER_LOGIN + userId);

        return ResponseResult.okResult(AppHttpCodeEnum.LOGOUT_SUCCESS);
    }

    @Override
    public ResponseResult getInfoByToken(String token) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            LoginUser loginUser = redisCache.getCacheObject(SysConstants.SYS_USER_LOGIN + userId);
            User user = loginUser.getUser();
            SysUserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, SysUserInfoVo.class);
          return ResponseResult.okResult(userInfoVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_FAIL);
    }

}

package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.lzhframework.constants.SysConstants;
import com.lzh.lzhframework.dao.UserMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.domain.entity.Role;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.domain.vo.AvatarUserVo;
import com.lzh.lzhframework.enums.AppHttpCodeEnum;
import com.lzh.lzhframework.domain.vo.SysUserInfoVo;
import com.lzh.lzhframework.exception.SystemException;
import com.lzh.lzhframework.form.LoginForm;
import com.lzh.lzhframework.service.SysUserService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import com.lzh.lzhframework.utils.JwtUtil;
import com.lzh.lzhframework.utils.RedisCache;
import com.lzh.lzhframework.utils.SecurityUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

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

    @Resource
    private UserMapper userMapper;

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

    @Override
    public List<Role> getUserRoles(Long userId) {
        return userMapper.selectRolesByUserId(userId);
    }

    @Override
    public ResponseResult updateUserProfile(User user) {
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPhonenumber())) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        // 校验手机号是否和其他用户重复
        User checkUser = userMapper.selectByEmail(user.getEmail());
        Long userId = user.getId();
        if (Objects.nonNull(checkUser)) {
            if (!checkUser.getId().equals(userId)) {
                throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
            }
        }
        checkUser = userMapper.selectByPhonenumber(user.getPhonenumber());
        if (Objects.nonNull(checkUser)) {
            if (!checkUser.getId().equals(userId)) {
                throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
            }
        }
        userMapper.updateById(user);

        // 更新缓存
        LoginUser loginUser = redisCache.getCacheObject(SysConstants.SYS_USER_LOGIN + userId);
        loginUser.setUser(userMapper.selectById(userId));
        redisCache.setCacheObject(SysConstants.SYS_USER_LOGIN + userId, loginUser);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult avatar() {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        User user = loginUser.getUser();
        AvatarUserVo avatarUserVo = BeanCopyUtils.copyBean(user, AvatarUserVo.class);
        List<Role> userRoles = this.getUserRoles(user.getId());

        StringBuilder sb = new StringBuilder();
        userRoles.forEach(role -> {
            sb.append(role.getRoleName());
            sb.append(",");
        });
        avatarUserVo.setRole(sb.substring(0, sb.length() - 1));
        return ResponseResult.okResult(avatarUserVo);
    }

    @Override
    public ResponseResult updatePwd(String oldPassword, String newPassword) {
        User user = SecurityUtils.getLoginUser().getUser();
        String password = user.getPassword();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(oldPassword, password)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.OLD_PWD_ERROR);
        }
        if (oldPassword.equals(newPassword)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.OLD_NEW_PWD_ALIKE);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        if (userMapper.updateById(user) > 0) {
            // 更新缓存用户密码
            LoginUser loginUser = redisCache.getCacheObject(SysConstants.SYS_USER_LOGIN + user.getId());
            loginUser.getUser().setPassword(passwordEncoder.encode(newPassword));
            redisCache.setCacheObject(SysConstants.SYS_USER_LOGIN + user.getId(), loginUser);
            return ResponseResult.okResult(AppHttpCodeEnum.UPDATE_PWD_SUCCESS);
        }
        return ResponseResult.errorResult("修改密码异常，请联系管理员");
    }

}
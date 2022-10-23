package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.constants.SysConstants;
import com.lzh.lzhblog.dao.UserMapper;
import com.lzh.lzhblog.domain.ResponseResult;
import com.lzh.lzhblog.domain.dto.UpdateEmailDTO;
import com.lzh.lzhblog.domain.dto.UpdatePwdDTO;
import com.lzh.lzhblog.domain.dto.UserDTO;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.LoginUser;
import com.lzh.lzhblog.domain.entity.User;
import com.lzh.lzhblog.enums.AppHttpCodeEnum;
import com.lzh.lzhblog.exception.SystemException;
import com.lzh.lzhblog.service.ArticleService;
import com.lzh.lzhblog.service.UserService;
import com.lzh.lzhblog.utils.BeanCopyUtils;
import com.lzh.lzhblog.utils.JwtUtil;
import com.lzh.lzhblog.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.lzh.lzhblog.constants.SysConstants.*;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-09-26 16:50:18
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public ResponseResult login(User user) {
        //用户名不能为空
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);

        redisCache.setCacheObject(PRE_LOGIN_USER_REDIS + userId, loginUser, 30, TimeUnit.MINUTES);

        User userInfo = loginUser.getUser();
        userInfo.setPassword("");


        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("userInfo", userInfo);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult getEmailCode(String email) {

        //验证码是否已经发送
        String emailKey = EMAIL_CODE + email;
        String emailCode = redisCache.getCacheObject(emailKey);
        if (StringUtils.hasText(emailCode)) {
            return ResponseResult.errorResult(309, "验证码已发送");
        }

        //发送验证码
        String sendEmailCode = sendCode(email);

        //将验证码存入redis
        redisCache.setCacheObject(emailKey, sendEmailCode, 1, TimeUnit.MINUTES);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult regist(UserDTO userDTO) {
        if (Objects.isNull(userDTO)) {
            return ResponseResult.errorResult(303, "注册失败");
        }
        if (!StringUtils.hasText(userDTO.getEmail())) {
            return ResponseResult.errorResult(304, "邮箱不能为空");
        }
        if (!StringUtils.hasText(userDTO.getPassword())) {
            return ResponseResult.errorResult(305, "密码不能为空");
        }
        if (!StringUtils.hasText(userDTO.getUserName())) {
            return ResponseResult.errorResult(306, "用户名不能为空");
        }

        boolean isRegisted = isRegisted(userDTO.getEmail());
        if (isRegisted) {
            return ResponseResult.errorResult(308, "该邮箱已经注册");
        }

        //验证码校验
        String redisEmailCode = redisCache.getCacheObject(EMAIL_CODE + userDTO.getEmail());
        if (!StringUtils.hasText(redisEmailCode)) {
            return ResponseResult.errorResult(309, "验证码失效");
        }
        if (!redisEmailCode.equals(userDTO.getCode())) {
            return ResponseResult.errorResult(310, "验证码输入错误");
        }

        //保存用户信息
        User user = BeanCopyUtils.copyBean(userDTO, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setAvatar("http://rir6wdlzn.hd-bkt.clouddn.com/default.jpg");
        save(user);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
//        String userId = SecurityUtils.getUserId().toString();

        redisCache.deleteObject(PRE_LOGIN_USER_REDIS + userId);
        SecurityContextHolder.getContext().setAuthentication(null);

        return ResponseResult.okResult();
    }

    @Override
    public User getUserByArticleId(Long articleId) {
        Article article = articleService.getArticleById(articleId);

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, article.getCreateBy());
        queryWrapper.eq(User::getStatus, "0");

        User user = getOne(queryWrapper);
        return user;
    }

    @Override
    public User getUserById(Long userId) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getStatus, "0");
        queryWrapper.eq(User::getId, userId);
        return getOne(queryWrapper);
    }

    @Override
    public ResponseResult updateUserAvatar(Long userId, String avatar) {
        User user = User.builder().id(userId).avatar(avatar).build();
        getBaseMapper().updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updatePasswordByUserId(UpdatePwdDTO updatePwdDTO) {
        User user = getById(updatePwdDTO.getUserId());
        //判断用户是否存在
        if (Objects.isNull(user)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_NOT_EXIT);
        }
        //校验当前密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(updatePwdDTO.getCurPassword(), user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CUR_PASSWORD_ERROR);
        }
        //判断两次密码输入是否正确
        if (!updatePwdDTO.getNewPassword().equals(updatePwdDTO.getConPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEW_CON_PASSWORD_NOT_MATCH);
        }

        //校验修改的密码是否为原密码
        if (encoder.matches(updatePwdDTO.getNewPassword(), user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEW_PASSWORD_REPEAT);
        }

        //更新密码
        String newPwdEncode = encoder.encode(updatePwdDTO.getNewPassword());
        user.setPassword(newPwdEncode);
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUpdateEmailCode(UpdateEmailDTO updateEmailDTO) {

        User user = getById(updateEmailDTO.getUserId());
        if (Objects.isNull(user)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_NOT_EXIT);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(updateEmailDTO.getCurPassword(), user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CUR_PASSWORD_ERROR);
        }
        if (!user.getEmail().equals(updateEmailDTO.getEmail())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_ERROR);
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(updateEmailDTO.getEmail());
        mailMessage.setSubject("PERSEVERE BLOG 账号验证");
        int sendEmailCode = (int) (Math.random() * ((999999 - 100000 + 1) + 100000));
        log.info("发送更新邮箱验证码-----------" + sendEmailCode);
        String context = "账号验证码为: " + sendEmailCode + ",一分钟内有效，请妥善保管!";
        mailMessage.setText(context);
        javaMailSender.send(mailMessage);

        //保存到redis
        redisCache.setCacheObject(EMAIL_UPDATE + updateEmailDTO.getEmail(), sendEmailCode + "", 1, TimeUnit.MINUTES);

        return ResponseResult.okResult(sendEmailCode + "");
    }

    @Override
    public ResponseResult checkCode(String code, String email) {
        String emailUpdateCode = redisCache.getCacheObject(EMAIL_UPDATE + email);
        if (!StringUtils.hasText(emailUpdateCode)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CODE_EXPIRE);
        }
        if (!emailUpdateCode.equals(code)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CODE_ERROR);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getNewEmailCode(UpdateEmailDTO updateEmailDTO) {
        String code = sendEmailCode(updateEmailDTO.getEmail(), "PERSEVERE BLOG 新邮箱验证", "验证码为：");
        log.info("新邮箱验证码----------------" + code);

        //保存到redis
        redisCache.setCacheObject(NEW_EMAIL_CODE + updateEmailDTO.getEmail(), code, 1, TimeUnit.MINUTES);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult finishEmailUpdate(UpdateEmailDTO updateEmailDTO) {

        String email = updateEmailDTO.getEmail();
        //校验验证码
        String code = redisCache.getCacheObject(NEW_EMAIL_CODE + email);
        if (!StringUtils.hasText(code)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CODE_EXPIRE);
        }
        if (!code.equals(updateEmailDTO.getCode())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CODE_ERROR);
        }
        //更新用户邮箱
        User user = User.builder().id(updateEmailDTO.getUserId()).email(updateEmailDTO.getEmail()).build();
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult checkEmail(UserDTO user) {
        String email = user.getEmail();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User one = getOne(queryWrapper);
        if (Objects.isNull(one)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_NOT_EXIT);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRePasswordCode(String email) {
        String code = sendEmailCode(email, "PERSEVERE BLOG 找回密码 邮箱验证", "验证码为：");
        log.info("找回密码，邮箱验证码----------------" + code);

        //保存到redis
        redisCache.setCacheObject(SysConstants.GET_PWD_CODE + email, code, 1, TimeUnit.MINUTES);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult rePassword(String email, String newPassword, String conPassword) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_NOT_EXIT);
        }

        if (!newPassword.equals(conPassword)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEW_CON_PASSWORD_NOT_MATCH);
        }

        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(User::getEmail, email);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode(conPassword);
        User user1 = new User();
        user1.setPassword(encode);

        update(user1, queryWrapper1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult checkRePwdCode(String email, String code) {

        String value = redisCache.getCacheObject(GET_PWD_CODE + email);

        if (!StringUtils.hasText(value)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CODE_EXPIRE);
        }
        if (!code.equals(value)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CODE_ERROR);
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult cancelAccount(Long userId) {
        //TODO 注销账户
        return null;
    }

    private String sendCode(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(email);
        mailMessage.setSubject("PERSEVERE BLOG");
        int sendEmailCode = (int) (Math.random() * ((999999 - 100000 + 1) + 100000));
        String context = "注册验证码为: " + sendEmailCode + ",一分钟内有效，请妥善保管!";
        mailMessage.setText(context);
        javaMailSender.send(mailMessage);

        return sendEmailCode + "";
    }

    private boolean isRegisted(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)) {
            return false;
        }
        return true;
    }

    private String sendEmailCode(String email, String subject, String preContext) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(fromEmail);
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        int sendEmailCode = (int) (Math.random() * ((999999 - 100000 + 1) + 100000));
        log.info("发送邮箱验证码-----------" + sendEmailCode);
        String context = preContext + sendEmailCode + ",一分钟内有效，请妥善保管!";
        mailMessage.setText(context);
        javaMailSender.send(mailMessage);

        return sendEmailCode + "";
    }
}


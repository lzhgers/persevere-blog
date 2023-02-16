package com.lzh.lzhblog.filter;

import com.alibaba.fastjson.JSON;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.LoginUser;
import com.lzh.lzhframework.utils.JwtUtil;
import com.lzh.lzhframework.utils.RedisCache;
import com.lzh.lzhframework.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            ResponseResult result = new ResponseResult();
            result.setCode(444);
            result.setMsg("登陆过期");
            WebUtils.renderString(response, JSON.toJSONString(response));
            throw new RuntimeException("token解析错误");
        }

        String userId = claims.getSubject();
        LoginUser loginUser = redisCache.getCacheObject("user:login:" + userId);
        if (Objects.isNull(loginUser)) {
            ResponseResult result = ResponseResult.okResult(444, "登陆过期");
            WebUtils.renderString(response, JSON.toJSONString(result));
            throw new RuntimeException("用户未登录");
        }

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}

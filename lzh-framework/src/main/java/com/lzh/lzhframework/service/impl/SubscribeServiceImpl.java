package com.lzh.lzhframework.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.SubscribeMapper;
import com.lzh.lzhframework.dao.UserMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.Subscribe;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.domain.vo.UserVo;
import com.lzh.lzhframework.service.SubscribeService;
import com.lzh.lzhframework.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户关注关系表(Subscribe)表服务实现类
 *
 * @author makejava
 * @since 2022-10-23 12:54:08
 */
@Service("subscribeService")
public class SubscribeServiceImpl extends ServiceImpl<SubscribeMapper, Subscribe> implements SubscribeService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseResult subscribeUser(Long subscribe, Long beSubscribe) {

        SubscribeMapper subscribeMapper = getBaseMapper();

        LambdaQueryWrapper<Subscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscribe::getSubscribe, subscribe);
        queryWrapper.eq(Subscribe::getBeSubscribe, beSubscribe);
        Subscribe sub = subscribeMapper.selectOne(queryWrapper);

        Map<String, Object> map = new HashMap<>();

        if (Objects.isNull(sub)) {
            //未关注过
            Subscribe one = new Subscribe()
                    .setBeSubscribe(beSubscribe)
                    .setSubscribe(subscribe);
            one.setStatus(0);
            subscribeMapper.insert(one);
            map.put("subStatus", 0);
        } else {
            //有关注过
            if (0 == sub.getStatus()) {
                //已关注
                sub.setStatus(1);
            } else {
                //未关注
                sub.setStatus(0);
            }
            subscribeMapper.updateById(sub);
        }
        map.put("subStatus", sub.getStatus());
        return ResponseResult.success(map);
    }

    @Override
    public ResponseResult pageUserSubscribedByUserId(Long userId, Integer pageNum, Integer pageSize) {
        //获取关注用户的id
        LambdaQueryWrapper<Subscribe> subscribeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subscribeLambdaQueryWrapper.eq(Subscribe::getSubscribe, userId);
        subscribeLambdaQueryWrapper.eq(Subscribe::getStatus, 0);
        List<Subscribe> subscribes = list(subscribeLambdaQueryWrapper);
        List<Long> userIdList = subscribes.stream().map(Subscribe::getBeSubscribe).collect(Collectors.toList());

        if (userIdList.size() <= 0) {
            return ResponseResult.success();
        }

        //查询所有关注的用户
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.in(User::getId, userIdList);
        userLambdaQueryWrapper.eq(User::getStatus, "0");
        Page<User> page = new Page<>(pageNum, pageSize);
        userMapper.selectPage(page, userLambdaQueryWrapper);

        List<UserVo> userVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserVo.class);

        userVos = userVos.stream()
                .map(userVo -> {
                    int isSubEach = isSubEach(userId, userVo.getId());
                    userVo.setIsFocusEach(isSubEach);
                    return userVo;
                }).collect(Collectors.toList());

        PageVo pageVo = new PageVo(page.getTotal(), userVos);
        return ResponseResult.success(pageVo);
    }

    @Override
    public ResponseResult noSubscribe(Long subscribe, Long beSubscribe) {
        LambdaQueryWrapper<Subscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscribe::getSubscribe, subscribe);
        queryWrapper.eq(Subscribe::getBeSubscribe, beSubscribe);
        Subscribe sub = getOne(queryWrapper);

        if (Objects.isNull(sub)) {
            throw new RuntimeException("关注的用户不存在");
        }

        sub.setStatus(1);
        updateById(sub);

        return ResponseResult.success();
    }

    @Override
    public ResponseResult pageUserFan(Long userId, Integer pageNum, Integer pageSize) {

        //查询用户粉丝id
        LambdaQueryWrapper<Subscribe> subscribeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subscribeLambdaQueryWrapper.eq(Subscribe::getBeSubscribe, userId);
        subscribeLambdaQueryWrapper.eq(Subscribe::getStatus, 0);
        List<Long> fanId = list(subscribeLambdaQueryWrapper).stream()
                .map(Subscribe::getSubscribe).collect(Collectors.toList());

        //查询粉丝
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.in(User::getId, fanId);
        userLambdaQueryWrapper.eq(User::getStatus, "0");
        userMapper.selectPage(page, userLambdaQueryWrapper);

        //封装信息
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserVo.class);
        userVos = userVos.stream()
                .map(userVo -> {
                    userVo.setIsFocusEach(isSubEach(userVo.getId(), userId));
                    return userVo;
                }).collect(Collectors.toList());

        PageVo pageVo = new PageVo(page.getTotal(), userVos);
        return ResponseResult.success(pageVo);
    }

    @Override
    public ResponseResult subscribeFan(Long userId, Long fanId) {

        //判断用户是否关注过该粉丝
        LambdaQueryWrapper<Subscribe> subscribeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        subscribeLambdaQueryWrapper.eq(Subscribe::getSubscribe, userId);
        subscribeLambdaQueryWrapper.eq(Subscribe::getBeSubscribe, fanId);
        Subscribe subscribe = getOne(subscribeLambdaQueryWrapper);

        if (Objects.isNull(subscribe)) {
            //用户不曾关注过该粉丝
            Subscribe sub = new Subscribe()
                    .setSubscribe(userId)
                    .setBeSubscribe(fanId)
                    .setStatus(0);
            save(sub);
        } else {
            //用户曾经关注过该粉丝
            subscribe.setStatus(0);
            updateById(subscribe);
        }

        return ResponseResult.success();
    }

    @Override
    public ResponseResult noSubEach(Long userId, Long fanId) {
        LambdaQueryWrapper<Subscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscribe::getSubscribe, userId);
        queryWrapper.eq(Subscribe::getBeSubscribe, fanId);
        Subscribe subscribe = getOne(queryWrapper);

        subscribe.setStatus(1);
        updateById(subscribe);
        return ResponseResult.success();
    }

    @Override
    public Long countFans(Long userId) {

        LambdaQueryWrapper<Subscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscribe::getBeSubscribe, userId);
        queryWrapper.eq(Subscribe::getStatus, 0);
        return count(queryWrapper);
    }

    @Override
    public ResponseResult countSubscribe(Long userId) {
        LambdaQueryWrapper<Subscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscribe::getSubscribe, userId);
        queryWrapper.eq(Subscribe::getStatus, 0);
        long count = count(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("countSubscribe", count);
        return ResponseResult.success(map);
    }


    private int isSubEach(Long userId, Long beSubscribeId) {
        LambdaQueryWrapper<Subscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscribe::getSubscribe, beSubscribeId);
        queryWrapper.eq(Subscribe::getBeSubscribe, userId);
        Subscribe subscribe = getOne(queryWrapper);

        if (Objects.isNull(subscribe)) {
            return 1;
        }
        if (0 != subscribe.getStatus()) {
            return 1;
        }
        return 0;
    }

}


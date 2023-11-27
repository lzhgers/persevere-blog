package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.LogMapper;
import com.lzh.lzhframework.domain.dto.QueryLogDto;
import com.lzh.lzhframework.domain.entity.LogEntity;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.service.LogService;
import com.lzh.lzhframework.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.lzh.lzhframework.constants.SysConstants.ABNORMAL;

/**
 * @Author luzhiheng
 * @Date 2023/11/23 16:47
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements LogService {

    @Resource
    private UserService userService;

    @Override
    public PageVo queryUserLogPage(QueryLogDto queryLogDto) {
        String beginDate = queryLogDto.getBeginDate();
        String endDate = queryLogDto.getEndDate();
        String userBehavior = queryLogDto.getUserBehavior();

        Page<LogEntity> page = new Page<>(queryLogDto.getPageNum(), queryLogDto.getPageSize());
        LambdaQueryWrapper<LogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userBehavior), LogEntity::getUserBehavior, userBehavior);
        queryWrapper.ge(StringUtils.hasText(beginDate), LogEntity::getCreateTime, beginDate);
        queryWrapper.le(StringUtils.hasText(endDate), LogEntity::getCreateTime, endDate);

        page(page, queryWrapper);
        return new PageVo(page.getTotal(), page.getRecords());
    }

    @Override
    public PageVo queryOperateLogPage(QueryLogDto queryLogDto) {
        String beginDate = queryLogDto.getBeginDate();
        String endDate = queryLogDto.getEndDate();
        String operatePerson = queryLogDto.getOperatePerson();
        String ip = queryLogDto.getIp();
        String requestInterface = queryLogDto.getRequestInterface();

        Page<LogEntity> page = new Page<>(queryLogDto.getPageNum(), queryLogDto.getPageSize());
        LambdaQueryWrapper<LogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(requestInterface), LogEntity::getRequestInterface, requestInterface);
        queryWrapper.like(StringUtils.hasText(ip), LogEntity::getIp, ip);
        if (StringUtils.hasText(operatePerson)) {
            List<User> userList = userService.listUserByName(operatePerson);
            List<String> userIdList = userList.stream().map(user -> String.valueOf(user.getId())).collect(Collectors.toList());
            queryWrapper.in(LogEntity::getOperatePerson, userIdList);
        }
        queryWrapper.ge(StringUtils.hasText(beginDate), LogEntity::getCreateTime, beginDate);
        queryWrapper.le(StringUtils.hasText(endDate), LogEntity::getCreateTime, endDate);

        page(page, queryWrapper);
        return new PageVo(page.getTotal(), page.getRecords());
    }

    @Override
    public PageVo queryAbnormalLogPage(QueryLogDto queryLogDto) {
        String beginDate = queryLogDto.getBeginDate();
        String endDate = queryLogDto.getEndDate();
        String ip = queryLogDto.getIp();

        Page<LogEntity> page = new Page<>(queryLogDto.getPageNum(), queryLogDto.getPageSize());
        LambdaQueryWrapper<LogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(ip), LogEntity::getIp, ip);
        queryWrapper.eq(LogEntity::getStatus, ABNORMAL);
        queryWrapper.ge(StringUtils.hasText(beginDate), LogEntity::getCreateTime, beginDate);
        queryWrapper.le(StringUtils.hasText(endDate), LogEntity::getCreateTime, endDate);

        page(page, queryWrapper);
        return new PageVo(page.getTotal(), page.getRecords());
    }
}

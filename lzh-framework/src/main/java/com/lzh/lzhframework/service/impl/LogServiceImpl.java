package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.LogMapper;
import com.lzh.lzhframework.domain.dto.QueryLogDto;
import com.lzh.lzhframework.domain.entity.LogEntity;
import com.lzh.lzhframework.domain.vo.PageVo;
import com.lzh.lzhframework.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.lzh.lzhframework.constants.SysConstants.ABNORMAL;

/**
 * @Author luzhiheng
 * @Date 2023/11/23 16:47
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements LogService {

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
        queryWrapper.orderByDesc(LogEntity::getCreateTime);

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
        queryWrapper.like(StringUtils.hasText(operatePerson), LogEntity::getOperatePerson, operatePerson);
        queryWrapper.ge(StringUtils.hasText(beginDate), LogEntity::getCreateTime, beginDate);
        queryWrapper.le(StringUtils.hasText(endDate), LogEntity::getCreateTime, endDate);
        queryWrapper.orderByDesc(LogEntity::getCreateTime);

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
        queryWrapper.orderByDesc(LogEntity::getCreateTime);

        page(page, queryWrapper);
        return new PageVo(page.getTotal(), page.getRecords());
    }
}

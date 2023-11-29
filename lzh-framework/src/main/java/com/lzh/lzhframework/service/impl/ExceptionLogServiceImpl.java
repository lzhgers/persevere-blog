package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.ExceptionLogMapper;
import com.lzh.lzhframework.domain.entity.ExceptionLogEntity;
import com.lzh.lzhframework.service.ExceptionLogService;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Author luzhiheng
 * @Date 2023/11/28 11:45
 */
@Service
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLogEntity> implements ExceptionLogService {
    @Override
    public Set<String> queryTodayIpNum(String formatDate) {
        return baseMapper.queryTodayIpNum(formatDate);
    }
}

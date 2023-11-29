package com.lzh.lzhframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.entity.ExceptionLogEntity;

import java.util.Set;


/**
 * (LzhExceptionLog)表服务接口
 *
 * @author makejava
 * @since 2023-11-28 11:44:44
 */
public interface ExceptionLogService extends IService<ExceptionLogEntity> {

    /**
     * 获取今日不同ip
     * @return
     */
    Set<String> queryTodayIpNum(String formatDate);
}


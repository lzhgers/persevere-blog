package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


/**
 * 日志表(LogEntity)表数据库访问层
 *
 * @author luzhiheng
 * @since 2023-11-23
 */
@Mapper
public interface LogMapper extends BaseMapper<LogEntity> {

    /**
     * 获取今日不同IP
     * @param formatDate yyyy-MM-dd
     * @return
     */
    Set<String> queryTodayIp(@Param("date") String formatDate);
}


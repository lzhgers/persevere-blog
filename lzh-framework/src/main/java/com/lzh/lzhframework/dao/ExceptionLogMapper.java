package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.ExceptionLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


/**
 * (LzhExceptionLog)表数据库访问层
 *
 * @author luzhiheng
 * @since 2023-11-28 11:44:43
 */
public interface ExceptionLogMapper extends BaseMapper<ExceptionLogEntity> {

    /**
     * 查询今日不同IP
     *
     * @param formatDate
     * @return
     */
    Set<String> queryTodayIpNum(@Param("date") String formatDate);

}


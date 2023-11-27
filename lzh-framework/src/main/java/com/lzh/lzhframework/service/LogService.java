package com.lzh.lzhframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.dto.QueryLogDto;
import com.lzh.lzhframework.domain.entity.LogEntity;
import com.lzh.lzhframework.domain.vo.PageVo;

/**
 * @Author luzhiheng
 * @Date 2023/11/23 16:47
 */
public interface LogService extends IService<LogEntity> {

    /**
     * 分页查询用户日志
     *
     * @param queryLogDto 查询条件
     */
    PageVo queryUserLogPage(QueryLogDto queryLogDto);

    /**
     * 分页查询操作日志
     *
     * @param queryLogDto 查询条件
     */
    PageVo queryOperateLogPage(QueryLogDto queryLogDto);

    /**
     * 分页查询异常日志
     *
     * @param queryLogDto 查询条件
     */
    PageVo queryAbnormalLogPage(QueryLogDto queryLogDto);
}

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
     * 分页查询用户操作日志
     *
     * @param queryLogDto 查询条件
     */
    PageVo queryUserLog(QueryLogDto queryLogDto);
}

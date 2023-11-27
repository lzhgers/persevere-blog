package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.FeedbackEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Feedback)表数据库访问层
 *
 * @author luzhiheng
 * @since 2023-11-27 11:47:41
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<FeedbackEntity> {

}


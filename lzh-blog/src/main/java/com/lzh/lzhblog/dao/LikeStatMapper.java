package com.lzh.lzhblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhblog.domain.entity.LikeStat;
import org.springframework.stereotype.Repository;


/**
 * 点赞统计表(LikeStat)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-09 18:23:42
 */
@Repository
public interface LikeStatMapper extends BaseMapper<LikeStat> {

}


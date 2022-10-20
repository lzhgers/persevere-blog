package com.lzh.lzhblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhblog.domain.entity.Collect;


/**
 * (Collect)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-19 10:51:24
 */
public interface CollectMapper extends BaseMapper<Collect> {

    void updateCollectStatus(Collect collect);
}


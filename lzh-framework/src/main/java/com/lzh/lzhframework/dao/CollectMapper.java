package com.lzh.lzhframework.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.Collect;
import org.springframework.stereotype.Repository;

/**
 * (Collect)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-19 10:51:24
 */
@Repository
public interface CollectMapper extends BaseMapper<Collect> {

    void updateCollectStatus(Collect collect);
}


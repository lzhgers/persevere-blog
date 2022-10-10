package com.lzh.lzhblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhblog.domain.entity.LikeStat;


/**
 * 点赞统计表(LikeStat)表服务接口
 *
 * @author makejava
 * @since 2022-10-09 18:23:43
 */
public interface LikeStatService extends IService<LikeStat> {

    Long getLikeCountByArticleId(Long articleId);

}


package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.dao.LikeStatMapper;
import com.lzh.lzhblog.domain.entity.Article;
import com.lzh.lzhblog.domain.entity.LikeStat;
import com.lzh.lzhblog.domain.entity.UserLike;
import com.lzh.lzhblog.service.LikeStatService;
import org.springframework.stereotype.Service;

/**
 * 点赞统计表(LikeStat)表服务实现类
 *
 * @author makejava
 * @since 2022-10-09 18:23:44
 */
@Service("likeStatService")
public class LikeStatServiceImpl extends ServiceImpl<LikeStatMapper, LikeStat> implements LikeStatService {

}


package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.LikeStatMapper;
import com.lzh.lzhframework.domain.entity.LikeStat;
import com.lzh.lzhframework.service.LikeStatService;
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


package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.UserLike;
import org.springframework.stereotype.Repository;


/**
 * 用户点赞表(UserLike)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-09 18:25:17
 */
@Repository
public interface UserLikeMapper extends BaseMapper<UserLike> {

    int updateUserLike(UserLike userLike);

}


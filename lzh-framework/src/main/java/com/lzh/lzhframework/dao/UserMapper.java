package com.lzh.lzhframework.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.lzhframework.domain.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-26 16:50:17
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<String> getPermissionsByUserId(Long userId);

    /**
     * 根据邮箱查询用户
     * @param email
     * @return
     */
    User selectByEmail(@Param("email") String email);
}


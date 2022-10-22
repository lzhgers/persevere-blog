package com.lzh.blog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.blog.admin.dao.UserLikeMapper;
import com.lzh.blog.admin.domain.entity.UserLike;
import com.lzh.blog.admin.service.UserLikeService;
import org.springframework.stereotype.Service;

@Service
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike> implements UserLikeService {

}

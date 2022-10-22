package com.lzh.blog.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.blog.admin.dao.ArticleTagMapper;
import com.lzh.blog.admin.domain.entity.ArticleTag;
import com.lzh.blog.admin.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper,ArticleTag> implements ArticleTagService {

}

package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhblog.dao.CollectMapper;
import com.lzh.lzhblog.domain.entity.Collect;
import com.lzh.lzhblog.service.CollectService;
import org.springframework.stereotype.Service;

/**
 * (Collect)表服务实现类
 *
 * @author makejava
 * @since 2022-10-19 10:51:28
 */
@Service("collectService")
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

}


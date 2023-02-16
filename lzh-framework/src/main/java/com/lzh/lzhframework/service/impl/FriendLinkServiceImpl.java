package com.lzh.lzhframework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.FriendLinkMapper;
import com.lzh.lzhframework.domain.entity.FriendLink;
import com.lzh.lzhframework.service.FriendLinkService;
import org.springframework.stereotype.Service;

/**
 * 友情链接表(FriendLink)表服务实现类
 *
 * @author makejava
 * @since 2022-10-26 10:59:40
 */
@Service("friendLinkService")
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

}


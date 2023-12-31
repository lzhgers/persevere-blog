package com.lzh.lzhframework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzh.lzhframework.domain.entity.FriendLink;


/**
 * 友情链接表(FriendLink)表服务接口
 *
 * @author makejava
 * @since 2022-10-26 10:59:39
 */
public interface FriendLinkService extends IService<FriendLink> {

    Integer getMaxSortFriendLink();
}


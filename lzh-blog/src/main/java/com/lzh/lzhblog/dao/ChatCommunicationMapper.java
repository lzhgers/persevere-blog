package com.lzh.lzhblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzh.common.domain.entity.ChatCommunication;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 聊天记录(ChatCommunication)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-08 14:07:16
 */
@Repository
public interface ChatCommunicationMapper extends BaseMapper<ChatCommunication> {

    int updateIsRead(@Param("fromUserName") String fromUserName,
                     @Param("toUserName") String toUserName);

}


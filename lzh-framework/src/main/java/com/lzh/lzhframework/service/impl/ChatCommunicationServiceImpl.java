package com.lzh.lzhframework.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.lzhframework.dao.ChatCommunicationMapper;
import com.lzh.lzhframework.domain.ResponseResult;
import com.lzh.lzhframework.domain.entity.ChatCommunication;
import com.lzh.lzhframework.domain.vo.MsgUserVo;
import com.lzh.lzhframework.service.ChatCommunicationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天记录(ChatCommunication)表服务实现类
 *
 * @author makejava
 * @since 2022-11-08 14:07:18
 */
@Service("chatCommunicationService")
public class ChatCommunicationServiceImpl extends ServiceImpl<ChatCommunicationMapper, ChatCommunication> implements ChatCommunicationService {


    @Override
    public ResponseResult listCommunicationsByUserId(String fromUserName, String toUserName) {
        LambdaQueryWrapper<ChatCommunication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatCommunication::getFromName, fromUserName)
                .and(i -> i.eq(ChatCommunication::getToName, toUserName))
                .or(i -> i.eq(ChatCommunication::getFromName, toUserName)
                        .and(i1 -> i1.eq(ChatCommunication::getToName, fromUserName)));

        queryWrapper.orderByAsc(ChatCommunication::getId);
        List<ChatCommunication> communicationList = list(queryWrapper);
        return ResponseResult.okResult(communicationList);
    }

    @Override
    public ResponseResult getMsgByFromAndToName(String toUserName, List<String> users) {

        LambdaQueryWrapper<ChatCommunication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatCommunication::getToName, toUserName);
        queryWrapper.eq(ChatCommunication::getIsRead, 1);

        List<MsgUserVo> userVos = new ArrayList<>();

        List<ChatCommunication> list = list(queryWrapper);

        for (String user : users) {
            MsgUserVo userVo = new MsgUserVo();
            userVo.setUsername(user);
            userVo.setIsRead(0);
            for (ChatCommunication chatCommunication : list) {
                if (user.equals(chatCommunication.getFromName())) {
                    userVo.setIsRead(1);
                    break;
                }
            }
            userVos.add(userVo);
        }

        return ResponseResult.okResult(userVos);
    }

    @Override
    public ResponseResult updateIsRead(String fromUserName, String toUserName) {
        int count = baseMapper.updateIsRead(toUserName, fromUserName);
        return ResponseResult.okResult();
    }

}

package com.lzh.lzhblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzh.common.domain.ResponseResult;
import com.lzh.common.domain.entity.ChatCommunication;
import com.lzh.common.domain.entity.User;
import com.lzh.common.domain.enums.AppHttpCodeEnum;
import com.lzh.common.domain.vo.MsgUserVo;
import com.lzh.common.utils.RedisCache;
import com.lzh.lzhblog.dao.ChatCommunicationMapper;
import com.lzh.lzhblog.dao.UserMapper;
import com.lzh.lzhblog.service.ChatCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.lzh.lzhblog.constants.SysConstants.MSG_RECEIVE_FROM;

/**
 * 聊天记录(ChatCommunication)表服务实现类
 *
 * @author makejava
 * @since 2022-11-08 14:07:18
 */
@Service("chatCommunicationService")
public class ChatCommunicationServiceImpl extends ServiceImpl<ChatCommunicationMapper, ChatCommunication> implements ChatCommunicationService {

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult listCommunicationsByUserId(String fromUserName, String toUserName) {
        LambdaQueryWrapper<ChatCommunication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChatCommunication::getFromName, fromUserName)
                .and(i -> i.eq(ChatCommunication::getToName, toUserName))
                .or(i -> i.eq(ChatCommunication::getFromName, toUserName)
                        .and(i1 -> i1.eq(ChatCommunication::getToName, fromUserName)));

        queryWrapper.orderByAsc(ChatCommunication::getTime);
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

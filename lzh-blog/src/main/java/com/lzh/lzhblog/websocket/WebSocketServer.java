package com.lzh.lzhblog.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzh.lzhframework.utils.SpringContextUtil;
import com.lzh.lzhframework.domain.entity.ChatCommunication;
import com.lzh.lzhframework.domain.entity.User;
import com.lzh.lzhframework.domain.vo.ConnectVo;
import com.lzh.lzhframework.domain.vo.SendMsgVo;
import com.lzh.lzhframework.service.ChatCommunicationService;
import com.lzh.lzhframework.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author websocket服务
 */
@ServerEndpoint(value = "/imserver/{username}")
@Component
public class WebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 记录当前在线连接数
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessionMap.put(username, session);
        log.info("有新用户加入，username={}, 当前在线人数为：{}", username, sessionMap.size());

        ConnectVo connectVo = new ConnectVo();
        List<Map<String, Object>> list = new ArrayList<>();
        connectVo.setUsers(list);

        for (Object key : sessionMap.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("username", key);
            // {"username", "zhang", "username": "admin"}
            list.add(map);
        }

//        {"users": [{"username": "zhang"},{ "username": "admin"}]}
        // 后台发送消息给所有的客户端
        sendAllMessage(JSON.toJSONString(connectVo));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessionMap.remove(username);
        log.info("有一连接关闭，移除username={}的用户session, 当前在线人数为：{}", username, sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        log.info("服务端收到用户username={}的消息:{}", username, message);

        JSONObject obj = JSONObject.parseObject(message);

        // to表示发送给哪个用户，比如 admin
        String toUsername = obj.getString("to");

        // 发送的消息文本  hello
        String text = obj.getString("text");

        UserService userService = (UserService) SpringContextUtil.getBean("userService");
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserName, username));

        ChatCommunication chatCommunication = new ChatCommunication()
                .setFromName(username)
                .setToName(toUsername)
                .setContent(text)
                .setFromAvatar(user.getAvatar())
                .setIsRead(1);

        ChatCommunicationService chatCommunicationService =
                (ChatCommunicationService) SpringContextUtil.getBean("chatCommunicationService");
        chatCommunicationService.save(chatCommunication);

        // 根据 to用户名来获取 session，再通过session发送消息文本
        // {"to": "admin", "text": "聊天文本"}
        Session toSession = sessionMap.get(toUsername);

        if (toSession != null) {
            // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
            // {"from": "zhang", "text": "hello"}

            SendMsgVo sendMsgVo = new SendMsgVo(username, text);
            // from 是 zhang
            // text 同上面的text
            this.sendMessage(JSON.toJSONString(sendMsgVo), toSession);
            log.info("发送给用户username={}，消息：{}", toUsername, JSON.toJSONString(sendMsgVo));
        } else {
            log.info("发送失败，未找到用户username={}的session", toUsername);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}

package org.atatame.service.handler;

import org.atatame.service.pojo.entity.Group;
import org.atatame.service.service.ChatSessionService;
import org.atatame.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    @Autowired
    private ChatSessionService sessionManager;
    @Autowired
    private UserService userService;

    @Override
    //处理客户端连接请求
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String uri= Objects.requireNonNull(session.getUri()).getPath();
        Long id = Long.valueOf(uri.substring(uri.lastIndexOf("/") + 1));
        if(userService.isUserExist(id)){
            sessionManager.addUserSessions(id,session);
            for(Group group:userService.getGroupsList(id)){
                sessionManager.addGroupSessions(group.getId(),session);
            }
            System.err.println("用户"+id+"已连接");
            session.sendMessage(new TextMessage("{\"type\": \"connected\", \"message\": \"连接成功\"}"));
        }else{
            session.sendMessage(new TextMessage("{\"type\": \"error\", \"message\": \"用户不存在\"}"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        session.sendMessage(new TextMessage("你好你好"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionManager.removeSession(session);
    }

}

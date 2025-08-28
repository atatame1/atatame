package org.atatame.service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class ChatSessionService {
    // 存储带用户ID的会话（用于单发）
    private final Map<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    //存储群聊会话
    private final Map<Long,Set<WebSocketSession>> groupSessions= new ConcurrentHashMap<>();

    public void addUserSessions(Long id,WebSocketSession session){
        userSessions.put(id,session);
    }

    public WebSocketSession getUserSessions(Long id){
        return userSessions.get(id);
    }

    public void removeUserSessions(Long id){
        userSessions.remove(id);
    }

    public void removeSession(WebSocketSession session) {
        userSessions.values().remove(session);
        // 从所有群聊中移除该会话
        for (Set<WebSocketSession> group : groupSessions.values()) {
            group.remove(session);
        }
    }

    public void addGroupSessions(Long id,WebSocketSession session){
        if(groupSessions.get(id)!=null){
            groupSessions.get(id).add(session);
        }else{
            Set<WebSocketSession>set=new CopyOnWriteArraySet<WebSocketSession>();
            set.add(session);
            groupSessions.put(id,set);
        }
    }

    public Set<WebSocketSession>getGroupSessions(Long id){
        return groupSessions.get(id);
    }

    public void removeGroupSessions(Long id){
        groupSessions.remove(id);
    }

}

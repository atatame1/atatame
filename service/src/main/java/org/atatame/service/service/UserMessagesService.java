package org.atatame.service.service;

import org.atatame.service.mapper.UserMessagesMapper;
import org.atatame.service.pojo.entity.UserMessages;
import org.atatame.service.pojo.request.UserMessagesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserMessagesService {
    @Autowired
    private UserMessagesMapper userMessagesMapper;

    public boolean saveMessages(Long userId, UserMessagesRequest messages,boolean selfFlag){
        return userMessagesMapper.insert(userId, messages.getFriendId(), messages.getForm(), selfFlag, messages.getContent(), LocalDateTime.now())>0;
    }

    public List<UserMessages> selectMessagesListByFriendId(Long userId,Long friendId){
        return userMessagesMapper.selectMessagesListByFriendId(userId,friendId);
    }

}

package org.atatame.service.service;

import org.atatame.service.mapper.GroupMessagesMapper;
import org.atatame.service.pojo.entity.GroupMessages;
import org.atatame.service.pojo.request.GroupMessagesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupMessagesService {
    @Autowired
    private GroupMessagesMapper groupMessagesMapper;

    public boolean saveMessages(Long userId,GroupMessagesRequest request){
        return groupMessagesMapper.insert(request.getGroupId(),request.getForm(),request.getContent(), userId, LocalDateTime.now())>0;
    }

    public List<GroupMessages>selectMessagesListByGroupId(Long groupId){
        return groupMessagesMapper.selectMessagesListByGroupId(groupId);
    }
}

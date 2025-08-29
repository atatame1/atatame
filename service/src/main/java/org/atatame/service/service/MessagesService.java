package org.atatame.service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.atatame.service.mapper.MessagesMapper;
import org.atatame.service.mapper.StructMapper;
import org.atatame.service.pojo.entity.messages;
import org.atatame.service.request.MessagesRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessagesService {
    @Autowired
    private MessagesMapper messagesMapper;

    public boolean saveMessages(messages messages){
        return messagesMapper.insert(messages)>0;
    }

    public List<messages> getMessagesByUser(Long selfId,Long otherId){
        LambdaQueryWrapper<messages>wrapper=new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(messages::getSenderId, selfId)
                        .eq(messages::getReceiverId, otherId))
                .or(w -> w.eq(messages::getSenderId, otherId)
                        .eq(messages::getReceiverId, selfId))
                .orderByAsc(messages::getDeliverTime); // 按时间排序

        return messagesMapper.selectList(wrapper);
    }

    public List<messages> getMessagesByGroup(Long groupId){
        LambdaQueryWrapper<messages>wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(messages::getGroupId,groupId)
                .orderByAsc(messages::getDeliverTime);
        return messagesMapper.selectList(wrapper);
    }

}

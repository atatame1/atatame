package org.atatame.service.service;

import org.atatame.service.mapper.GroupMapper;
import org.atatame.service.pojo.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    public Group selectByGroupId(Long groupId){
        return groupMapper.selectById(groupId);
    }
}

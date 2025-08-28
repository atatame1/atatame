package org.atatame.service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.atatame.service.mapper.UserGroupMapper;
import org.atatame.service.pojo.entity.user_group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGroupService {
    @Autowired
    private UserGroupMapper mapper;

    public List<Long> getGroupIdByUserId(Long userId){
        return mapper.selectGroupIdByUserId(userId);
    }


}

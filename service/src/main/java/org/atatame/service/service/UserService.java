package org.atatame.service.service;

import org.atatame.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public boolean isUserExist(Long userId){
        return userMapper.selectById(userId)!=null;
    }
}

package org.atatame.service.service;

import org.atatame.service.mapper.UserMapper;
import org.atatame.service.pojo.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean isUserExist(Long userId){
        return userMapper.selectById(userId)!=null;
    }

    public boolean isUserNameExist(String username){
        return userMapper.selectByName(username)!=null;
    }
    public boolean insert(String username,String password){
        user user=new user();
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        return userMapper.insert(user)>0;
    }
}

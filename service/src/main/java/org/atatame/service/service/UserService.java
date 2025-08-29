package org.atatame.service.service;

import org.atatame.service.mapper.UserFriendsMapper;
import org.atatame.service.mapper.UserGroupMapper;
import org.atatame.service.mapper.UserMapper;
import org.atatame.service.pojo.entity.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFriendsMapper userFriendsMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean isUserExist(Long userId){
        return userMapper.selectById(userId)!=null;
    }

    public boolean isUserNameExist(String username){
        return userMapper.selectByName(username)!=null;
    }
    public boolean register(String username, String password){
        user user=new user();
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        return userMapper.insert(user)>0;
    }

    public List<Long> getFriendsList(Long id){
        return userFriendsMapper.selectListByUserId(id);
    }

    public List<Long> getGroupsList(Long userId){
        return userGroupMapper.selectGroupIdByUserId(userId);
    }
}

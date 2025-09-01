package org.atatame.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.atatame.service.pojo.entity.User;
import org.atatame.service.pojo.entity.UserFriends;

import java.util.List;

@Mapper
public interface UserFriendsMapper extends BaseMapper<UserFriends> {

    @Select("select friend_id from user_friends where user_id=#{id}")
    List<User> selectListByUserId(Long id);
}

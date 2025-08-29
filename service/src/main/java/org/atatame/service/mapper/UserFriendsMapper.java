package org.atatame.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.atatame.service.pojo.entity.user_friends;

import java.util.List;

@Mapper
public interface UserFriendsMapper extends BaseMapper<user_friends> {

    @Select("select friend_id from user_friends where user_id=#{id}")
    List<Long> selectListByUserId(Long id);
}

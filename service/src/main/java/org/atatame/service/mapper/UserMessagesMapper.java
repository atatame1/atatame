package org.atatame.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.atatame.service.pojo.entity.UserMessages;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMessagesMapper extends BaseMapper<UserMessages> {


    @Insert("INSERT INTO user_messages_#{userId} (form,friend_id, self_flag, content, deliver_time) VALUES (#{form},#{friendId}, #{selfFlag}, #{content}, #{deliverTime})")
    int insert(@Param("userId") Long userId, @Param("friendId") Long friendId,@Param("form") String form,
                @Param("selfFlag") boolean selfFlag, @Param("content") String content,
                @Param("deliverTime") LocalDateTime deliverTime);

    @Select("SELECT * from user_messages_#{userId} where friend_id=#{friendId}")
    List<UserMessages> selectMessagesListByFriendId(@Param("userId")Long userId,@Param("friendId")Long friendId);
}

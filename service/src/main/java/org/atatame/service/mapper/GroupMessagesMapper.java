package org.atatame.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.atatame.service.pojo.entity.GroupMessages;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface GroupMessagesMapper extends BaseMapper<GroupMessages> {

    @Insert("insert into group_messages_#{groupId} (form,content,sedner_id,delivet_time) values (#{form},#{content},#{senderId},#{deliverTime})")
    int insert(@Param("groupId")Long groupId, @Param("form")String form, @Param("content")String content,
               @Param("senderId")Long senderId, @Param("deliverTime")LocalDateTime deliverTime);


    @Select("select * from group_messages_#{groupId}")
    List<GroupMessages> selectMessagesListByGroupId(Long groupId);
}

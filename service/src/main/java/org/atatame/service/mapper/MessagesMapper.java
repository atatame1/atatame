package org.atatame.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.atatame.service.pojo.entity.messages;

import java.util.List;

@Mapper
public interface MessagesMapper extends BaseMapper<messages> {

    @Select("SELECT id, sender_id, receiver_id, content, deliver_time FROM messages " +
            "WHERE (sender_id = #{selfId} AND receiver_id = #{otherId}) " +
            "UNION ALL " +
            "SELECT id, sender_id, receiver_id, content, deliver_time FROM messages " +
            "WHERE (sender_id = #{otherId} AND receiver_id = #{selfId}) " +
            "ORDER BY deliver_time ASC")
    List<messages> selectUserMessages(@Param("selfId") Long selfId,@Param("otherId")Long otherId);
}

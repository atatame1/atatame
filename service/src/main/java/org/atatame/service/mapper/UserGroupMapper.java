package org.atatame.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.atatame.service.pojo.entity.user_group;

import java.util.List;

@Mapper
public interface UserGroupMapper extends BaseMapper<user_group> {

    @Select("select group_id from user_group where user_id=#{userId}")
    List<Long> selectGroupIdByUserId(Long userId);
}

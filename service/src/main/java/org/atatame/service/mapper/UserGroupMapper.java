package org.atatame.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.atatame.service.pojo.entity.Group;
import org.atatame.service.pojo.entity.UserGroup;

import java.util.List;

@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroup> {

    @Select("select group_id from user_group where user_id=#{userId}")
    List<Group> selectGroupIdByUserId(Long userId);
}

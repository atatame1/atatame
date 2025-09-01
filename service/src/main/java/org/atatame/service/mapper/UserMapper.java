package org.atatame.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.atatame.service.pojo.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where name=#{name}")
    User selectByName(String name);
}

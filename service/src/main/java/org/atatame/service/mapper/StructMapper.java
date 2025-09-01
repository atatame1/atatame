package org.atatame.service.mapper;

import org.atatame.service.pojo.entity.Group;
import org.atatame.service.pojo.entity.GroupMessages;
import org.atatame.service.pojo.entity.User;
import org.atatame.service.pojo.entity.UserMessages;
import org.atatame.service.pojo.vo.GroupMessagesVo;
import org.atatame.service.pojo.vo.GroupVo;
import org.atatame.service.pojo.vo.UserMessagesVo;
import org.atatame.service.pojo.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StructMapper {
    StructMapper INSTANCE = Mappers.getMapper(StructMapper.class);

    List<UserMessagesVo> toUserMessagesVoList(List<UserMessages>ls);

    UserMessagesVo toUserMessagesVo(UserMessages m);

    List<GroupMessagesVo> toGroupMessagesVoList(List<GroupMessages>ls);

    GroupMessagesVo toGroupMessagesVo(GroupMessages m);

    UserVo toUserVo(User u);

    List<UserVo> toUserVoList(List<User>ls);

    GroupVo toGroupVo(Group g);

    List<GroupVo> toGroupVoList(List<Group>ls);

}

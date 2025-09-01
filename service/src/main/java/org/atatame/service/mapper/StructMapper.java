package org.atatame.service.mapper;

import org.atatame.service.pojo.entity.group;
import org.atatame.service.pojo.entity.messages;
import org.atatame.service.pojo.entity.user;
import org.atatame.service.pojo.vo.GroupVo;
import org.atatame.service.pojo.vo.MessagesVo;
import org.atatame.service.pojo.request.MessagesRequest;
import org.atatame.service.pojo.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StructMapper {
    StructMapper INSTANCE = Mappers.getMapper(StructMapper.class);

    messages toMessages(MessagesRequest vo);

    List<MessagesVo> toMessagesVoList(List<messages>ls);

    MessagesVo toMessagesVo(messages m);

    UserVo toUserVo(user u);

    List<UserVo> toUserVoList(List<user>ls);

    GroupVo toGroupVo(group g);

    List<GroupVo> toGroupVoList(List<group>ls);

}

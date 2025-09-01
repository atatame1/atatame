package org.atatame.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;
import org.atatame.service.mapper.StructMapper;
import org.atatame.service.pojo.vo.GroupMessagesVo;
import org.atatame.service.pojo.vo.UserMessagesVo;
import org.atatame.service.service.GroupMessagesService;
import org.atatame.service.service.UserMessagesService;
import org.atatame.common.result.Result;
import org.atatame.service.userIml.UserDetailsIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "获得消息记录")
@RestController
@RequestMapping("/api/messages/get")
public class MessagesController {

    @Autowired
    private UserMessagesService userMessagesService;

    @Autowired
    private GroupMessagesService groupMessagesService;

    @Operation(summary = "获得个人消息")
    @GetMapping("/user")
    public Result<List<UserMessagesVo>> getUserMessagesRecords(@AuthenticationPrincipal UserDetailsIml user, @Param("friendId") Long friendId){
        return Result.ok(StructMapper.INSTANCE.toUserMessagesVoList(userMessagesService.selectMessagesListByFriendId(user.getId(),friendId)));
    }

    @Operation(summary = "获得群聊消息")
    @GetMapping("/group")
    public Result<List<GroupMessagesVo>> getGroupMessagesRecords(@Param("groupId")Long groupId){
        return Result.ok(StructMapper.INSTANCE.toGroupMessagesVoList(groupMessagesService.selectMessagesListByGroupId(groupId)));
    }
}

package org.atatame.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;
import org.atatame.service.mapper.StructMapper;
import org.atatame.service.pojo.vo.MessagesVo;
import org.atatame.service.service.MessagesService;
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
    private MessagesService messagesService;

    @Operation(summary = "获得个人消息")
    @GetMapping("/user")
    public Result<List<MessagesVo>> getUserMessagesRecords(@AuthenticationPrincipal UserDetailsIml user, @Param("otherId") Long otherId){
        return Result.ok(StructMapper.INSTANCE.toMessagesVoList(messagesService.getMessagesByUser(user.getId(),otherId)));
    }

    @Operation(summary = "获得群聊消息")
    @GetMapping("/group")
    public Result<List<MessagesVo>> getGroupMessagesRecords(@Param("groupId")Long groupId){
        return Result.ok(StructMapper.INSTANCE.toMessagesVoList(messagesService.getMessagesByGroup(groupId)));
    }
}

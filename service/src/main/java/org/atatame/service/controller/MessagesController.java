package org.atatame.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;
import org.atatame.service.mapper.StructMapper;
import org.atatame.service.service.MessagesService;
import org.atatame.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "获得消息记录")
@RestController
@RequestMapping("/api/messages/get")
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @Operation(summary = "获得个人消息")
    @GetMapping("/group")
    public Result<Object> getUserMessagesRecords(@Param("selfId")Long selfId,@Param("otherId") Long otherId){
        return Result.ok(StructMapper.INSTANCE.toMessagesVoList(messagesService.getMessagesByUser(selfId,otherId)));
    }

    @Operation(summary = "获得群聊消息")
    @GetMapping("/user")
    public Result<Object> getGroupMessagesRecords(@Param("groupId")Long groupId){
        return Result.ok(StructMapper.INSTANCE.toMessagesVoList(messagesService.getMessagesByGroup(groupId)));
    }
}

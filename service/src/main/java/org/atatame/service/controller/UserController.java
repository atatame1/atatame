package org.atatame.service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.atatame.common.result.Result;
import org.atatame.service.mapper.StructMapper;
import org.atatame.service.pojo.vo.GroupVo;
import org.atatame.service.pojo.vo.UserVo;
import org.atatame.service.service.UserService;
import org.atatame.service.userIml.UserDetailsIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户相关操作")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获得好友列表")
    @GetMapping("/friends")
    public Result<List<UserVo>> getFriendsList(@AuthenticationPrincipal UserDetailsIml user){
        return Result.ok(StructMapper.INSTANCE.toUserVoList(userService.getFriendsList(user.getId())));
    }

    @Operation(summary = "获得群聊列表")
    @GetMapping("/groups")
    public Result<List<GroupVo>> getGroupsList(@AuthenticationPrincipal UserDetailsIml user){
        return Result.ok(StructMapper.INSTANCE.toGroupVoList(userService.getGroupsList(user.getId())));
    }
}

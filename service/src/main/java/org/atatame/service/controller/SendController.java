package org.atatame.service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.atatame.service.mapper.StructMapper;
import org.atatame.service.pojo.request.GroupMessagesRequest;
import org.atatame.service.pojo.request.UserMessagesRequest;
import org.atatame.service.service.*;
import org.atatame.common.enums.MessagesTypeEnum;
import org.atatame.common.result.Result;
import org.atatame.common.enums.ResultEnum;
import org.atatame.service.userIml.UserDetailsIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Tag(name = "消息发送")
@RestController
@RequestMapping("/api/messages/send")
public class SendController {

    @Autowired
    private ChatSessionService sessionManager;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserMessagesService userMessagesService;

    @Autowired
    private GroupMessagesService groupMessagesService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());//配置LocalDateTime类型的序列化器

    @Operation(summary = "发送至好友")
    @PostMapping("/friend")
    public Result<Object>sendMessagesToFriend(@AuthenticationPrincipal UserDetailsIml user, @RequestBody UserMessagesRequest request){
        try{
            if(userService.isUserExist(request.getFriendId()))return Result.error(ResultEnum.USER_NOT_EXISTED);

            String messageJson = objectMapper.writeValueAsString(request);

            WebSocketSession targetSession = sessionManager.getUserSessions(request.getFriendId());
            userMessagesService.saveMessages(user.getId(),request,true);

            UserMessagesRequest back=new UserMessagesRequest(request.getForm(), request.getContent(), user.getId());
            userMessagesService.saveMessages(request.getFriendId(), back,false);

            //发给指定用户
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(messageJson));
            }
            return Result.ok();
        } catch (NullPointerException e) {
            return Result.error(ResultEnum.PARAMS_EMPTY);
        } catch (Exception e) {
            return Result.error(e);
        }
    }
    @Operation(summary = "发送至群聊")
    @PostMapping("/group")
    public Result<Object>sendMessagesToGroup(@AuthenticationPrincipal UserDetailsIml user,@RequestBody GroupMessagesRequest request){
        try{
            if(groupService.selectByGroupId(request.getGroupId())==null)return Result.error(ResultEnum.GROUP_NOT_EXIST);

            String messageJson = objectMapper.writeValueAsString(request);
            groupMessagesService.saveMessages(user.getId(), request);
            for (WebSocketSession session : sessionManager.getGroupSessions(request.getGroupId())) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(messageJson));
                }
            }
            return Result.ok();
        }catch (NullPointerException e){
            return Result.error(ResultEnum.PARAMS_EMPTY);
        }catch (Exception e){
            return Result.error(e);
        }

    }
}

package org.atatame.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.atatame.service.mapper.StructMapper;
import org.atatame.service.request.MessagesRequest;
import org.atatame.service.service.ChatSessionService;
import org.atatame.service.service.MessagesService;
import org.atatame.common.enums.MessagesTypeEnum;
import org.atatame.common.result.Result;
import org.atatame.common.enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MessagesService messagesService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());//配置LocalDateTime类型的序列化器

    @Operation(summary = "发送消息")
    @PostMapping
    public Result<Object>sendMessage(@RequestBody MessagesRequest request){
        try{
            String messageJson = objectMapper.writeValueAsString(request);
            //发给指定用户
            if(MessagesTypeEnum.USER_CHAT.equals(request.getType())){
                WebSocketSession targetSession = sessionManager.getUserSessions(request.getReceiverId());
                messagesService.saveMessages(StructMapper.INSTANCE.toMessages(request));
                if (targetSession == null || !targetSession.isOpen()) {
                    return Result.error();
                }
                targetSession.sendMessage(new TextMessage(messageJson));
            }

            //发给群聊
            else if(MessagesTypeEnum.GROUP_CHAT.equals(request.getType())){
                // 发送给所有连接的客户端
                messagesService.saveMessages(StructMapper.INSTANCE.toMessages(request));
                for (WebSocketSession session : sessionManager.getGroupSessions(request.getReceiverId())) {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(messageJson));
                    }
                }
            }
            return Result.error();
        } catch (NullPointerException e) {
            return Result.error(ResultEnum.PARAMS_EMPTY);
        } catch (Exception e) {
            return Result.error(e);
        }
    }
}

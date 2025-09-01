package org.atatame.service.pojo.vo;

import lombok.Data;
import lombok.NonNull;
import java.time.LocalDateTime;

@Data
public class MessagesVo {
    private String content;

    @NonNull
    private String form;//消息内容的类型，text

    @NonNull
    private String type;//消息的种类，群聊单独

    @NonNull
    private Long senderId;

    @NonNull
    private Long receiverId;

    @NonNull
    private LocalDateTime deliverTime;
}

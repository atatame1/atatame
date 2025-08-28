package org.atatame.service.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessagesDto {

    private String form;

    private String content;

    private Long groupId;

    private Long receiverId;

    private Long senderId;

    private LocalDateTime deliverTime;


}

package org.atatame.service.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMessagesVo {
    private String form;

    private String content;

    private Long senderId;

    private LocalDateTime deliverTime;
}

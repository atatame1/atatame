package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "messages")
public class messages {
    @TableId
    private Long id;

    private String form;

    private String type;

    private String content;

    private Long senderId;

    private Long receiverId;

    private Long groupId;

    private LocalDateTime deliverTime;

    @TableLogic
    private byte isDeleted;
}

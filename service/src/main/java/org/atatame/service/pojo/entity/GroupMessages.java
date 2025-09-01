package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("group_messages_#{groupId}")
public class GroupMessages {
    @TableId
    private Long id;

    private String form;

    private String content;

    private Long senderId;

    private LocalDateTime deliverTime;

    @TableLogic
    private byte isDeleted;
}

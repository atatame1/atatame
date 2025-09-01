package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user_messages_#{userId}")
public class UserMessages {
    @TableId
    private Long id;

    private String form;

    private String content;

    private Long friendId;

    private Boolean selfFlag;

    private LocalDateTime deliverTime;

    @TableLogic
    private byte isDeleted;
}

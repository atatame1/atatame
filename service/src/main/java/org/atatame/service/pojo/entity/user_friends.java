package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user_friends")
public class user_friends {

    private Long id;

    private Long userId;

    private Long friendId;

    private LocalDateTime createdTime;

    @TableLogic
    private byte isDeleted;
}

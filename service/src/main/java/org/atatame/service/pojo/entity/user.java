package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user")
public class user {
    @TableId
    private Long id;

    private String name;

    private String password;

    private boolean enable;

    private LocalDateTime createdTime;

    @TableLogic
    private byte isDeleted;
}

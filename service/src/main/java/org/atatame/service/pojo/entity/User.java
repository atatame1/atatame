package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "User")
public class User {
    @TableId
    private Long id;

    private String name;

    private String password;

    private Boolean isEnabled;

    private LocalDateTime createdTime;

    @TableLogic
    private byte isDeleted;
}

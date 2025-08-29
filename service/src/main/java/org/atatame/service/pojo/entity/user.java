package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user")
public class user {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String password;

    private Boolean enable;

    private LocalDateTime createdTime;

    @TableLogic
    private byte isDeleted;
}

package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user_group")
public class user_group {
    @TableId()
    private Long id;

    private Long userId;

    private Long groupId;

    @TableLogic
    private byte isDeleted;
}

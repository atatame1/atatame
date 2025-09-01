package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "UserGroup")
public class UserGroup {
    @TableId()
    private Long id;

    private Long userId;

    private Long groupId;

    @TableLogic
    private byte isDeleted;
}

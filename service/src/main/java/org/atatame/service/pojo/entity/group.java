package org.atatame.service.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "group")
public class group {
    @TableId
    private Long id;

    private String name;

    private int num;

    private LocalDateTime createTime;

    @TableLogic
    private byte isDeleted;

}

package org.atatame.service.pojo.vo;

import lombok.Data;
import lombok.NonNull;
import java.time.LocalDateTime;

@Data
public class UserMessagesVo {

    private String form;//消息内容的类型，text

    private String content;

    private Long friendId;

    private Boolean selfFlag;

    private LocalDateTime deliverTime;
}

package org.atatame.service.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class UserMessagesRequest {
    @NonNull
    private String form;//消息内容的类型，text

    private String content;
    @NonNull
    private Long friendId;
}

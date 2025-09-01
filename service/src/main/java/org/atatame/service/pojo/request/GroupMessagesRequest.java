package org.atatame.service.pojo.request;

import lombok.Data;

@Data
public class GroupMessagesRequest {
    private String form;

    private String content;

    private Long groupId;
}

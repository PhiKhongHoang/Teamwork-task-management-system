package com.ktn3.TTMS.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ResComment {
    private Long id;
    private Long taskId;
    private Long userId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;
    private List<String> attachmentUrls;
    private List<Long> mentionUserIds;
}

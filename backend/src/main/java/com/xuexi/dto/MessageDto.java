package com.xuexi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDto {
    private Long id;
    private String type;
    private String title;
    private String content;
    private String icon;
    private String timeAgo;
    private Boolean isRead;
    private LocalDateTime createdAt;
}

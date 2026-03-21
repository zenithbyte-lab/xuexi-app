package com.xuexi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String content;
    private String timeAgo;
    private LocalDateTime createdAt;
}

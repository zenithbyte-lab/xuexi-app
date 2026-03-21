package com.xuexi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long id;
    private Long userId;
    private String nickname;
    private String avatarUrl;
    private String type;
    private String content;
    private String imageUrl;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean liked;
    private String timeAgo;
    private LocalDateTime createdAt;
}

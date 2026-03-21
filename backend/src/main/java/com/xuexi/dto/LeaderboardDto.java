package com.xuexi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardDto {
    private Integer rank;
    private String nickname;
    private String avatarUrl;
    private Integer score;
    private Boolean isMe;
}

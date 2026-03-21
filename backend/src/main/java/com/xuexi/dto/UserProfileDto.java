package com.xuexi.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private Long id;
    private String nickname;
    private String avatarUrl;
    private Integer points;
    private Integer completedCourses;
    private Integer streakDays;
}

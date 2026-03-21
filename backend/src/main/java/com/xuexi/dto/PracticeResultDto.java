package com.xuexi.dto;

import lombok.Data;

@Data
public class PracticeResultDto {
    private Integer score;
    private Integer correctCount;
    private Integer total;
    private Integer timeSec;
}

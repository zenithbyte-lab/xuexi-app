package com.xuexi.dto;

import lombok.Data;
import java.util.List;

@Data
public class CheckinStatsDto {
    private Integer streakDays;
    private Boolean checkedToday;
    private List<String> monthCalendar;
}

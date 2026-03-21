package com.xuexi.controller;

import com.xuexi.dto.ApiResponse;
import com.xuexi.dto.CheckinStatsDto;
import com.xuexi.entity.Checkin;
import com.xuexi.service.CheckinService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkin")
@RequiredArgsConstructor
public class CheckinController {

    private final CheckinService checkinService;

    @PostMapping
    public ApiResponse<Checkin> doCheckin(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(checkinService.doCheckin(userId));
    }

    @GetMapping("/stats")
    public ApiResponse<CheckinStatsDto> getStats(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(checkinService.getStats(userId));
    }
}

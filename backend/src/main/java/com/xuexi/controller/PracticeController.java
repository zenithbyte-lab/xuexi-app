package com.xuexi.controller;

import com.xuexi.dto.ApiResponse;
import com.xuexi.dto.LeaderboardDto;
import com.xuexi.dto.PracticeResultDto;
import com.xuexi.dto.PracticeSubmitRequest;
import com.xuexi.entity.QuizQuestion;
import com.xuexi.service.PracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeController {

    private final PracticeService practiceService;

    @GetMapping("/today")
    public ApiResponse<List<QuizQuestion>> getTodayPractice() {
        return ApiResponse.ok(practiceService.getTodayQuestions());
    }

    @PostMapping("/submit")
    public ApiResponse<PracticeResultDto> submit(Authentication auth,
                                                   @RequestBody PracticeSubmitRequest req) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(practiceService.submitAnswers(userId, req.getAnswers()));
    }

    @GetMapping("/leaderboard")
    public ApiResponse<List<LeaderboardDto>> leaderboard(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(practiceService.getLeaderboard(userId));
    }
}

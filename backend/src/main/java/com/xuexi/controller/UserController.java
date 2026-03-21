package com.xuexi.controller;

import com.xuexi.dto.ApiResponse;
import com.xuexi.dto.UserProfileDto;
import com.xuexi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ApiResponse<UserProfileDto> getProfile(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(userService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ApiResponse<UserProfileDto> updateProfile(Authentication auth,
                                                      @RequestBody UserProfileDto dto) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(userService.updateProfile(userId, dto));
    }
}

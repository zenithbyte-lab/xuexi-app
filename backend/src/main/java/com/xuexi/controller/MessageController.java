package com.xuexi.controller;

import com.xuexi.dto.ApiResponse;
import com.xuexi.dto.MessageDto;
import com.xuexi.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ApiResponse<List<MessageDto>> getMessages(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(messageService.getMessages(userId));
    }

    @GetMapping("/unread-count")
    public ApiResponse<Long> getUnreadCount(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(messageService.getUnreadCount(userId));
    }

    @PutMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(Authentication auth, @PathVariable Long id) {
        Long userId = (Long) auth.getPrincipal();
        messageService.markAsRead(userId, id);
        return ApiResponse.ok();
    }
}

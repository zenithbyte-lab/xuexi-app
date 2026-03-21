package com.xuexi.controller;

import com.xuexi.dto.ApiResponse;
import com.xuexi.dto.CommentDto;
import com.xuexi.dto.PostDto;
import com.xuexi.dto.PostRequest;
import com.xuexi.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping
    public ApiResponse<List<PostDto>> getPosts(
            @RequestParam(required = false) String type) {
        return ApiResponse.ok(communityService.getPosts(type));
    }

    @PostMapping
    public ApiResponse<PostDto> createPost(Authentication auth,
                                            @Valid @RequestBody PostRequest req) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(communityService.createPost(userId, req));
    }

    @PostMapping("/{id}/like")
    public ApiResponse<Void> toggleLike(Authentication auth,
                                         @PathVariable Long id) {
        Long userId = (Long) auth.getPrincipal();
        communityService.toggleLike(userId, id);
        return ApiResponse.ok();
    }

    @GetMapping("/{id}/comments")
    public ApiResponse<List<CommentDto>> getComments(@PathVariable Long id) {
        return ApiResponse.ok(communityService.getComments(id));
    }

    @PostMapping("/{id}/comments")
    public ApiResponse<CommentDto> addComment(Authentication auth,
                                               @PathVariable Long id,
                                               @RequestBody Map<String, String> body) {
        Long userId = (Long) auth.getPrincipal();
        return ApiResponse.ok(communityService.addComment(userId, id, body.get("content")));
    }
}

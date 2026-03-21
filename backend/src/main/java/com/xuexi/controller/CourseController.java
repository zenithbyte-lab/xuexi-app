package com.xuexi.controller;

import com.xuexi.dto.ApiResponse;
import com.xuexi.entity.Course;
import com.xuexi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ApiResponse<List<Course>> getCourses(
            @RequestParam(required = false) String category) {
        return ApiResponse.ok(courseService.getCourses(category));
    }

    @GetMapping("/{id}")
    public ApiResponse<Course> getCourseDetail(@PathVariable Long id) {
        return ApiResponse.ok(courseService.getCourseDetail(id));
    }

    @PutMapping("/{id}/progress")
    public ApiResponse<Void> updateProgress(Authentication auth,
                                             @PathVariable Long id,
                                             @RequestBody Map<String, Integer> body) {
        Long userId = (Long) auth.getPrincipal();
        courseService.updateProgress(userId, id,
                body.get("currentChapter"),
                body.get("progressPct"));
        return ApiResponse.ok();
    }
}

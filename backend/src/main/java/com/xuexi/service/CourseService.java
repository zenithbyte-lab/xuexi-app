package com.xuexi.service;

import com.xuexi.entity.Course;
import com.xuexi.entity.UserCourseProgress;
import com.xuexi.entity.UserCourseProgressId;
import com.xuexi.repository.CourseRepository;
import com.xuexi.repository.UserCourseProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserCourseProgressRepository progressRepository;

    public List<Course> getCourses(String category) {
        if (category != null && !category.isEmpty()) {
            return courseRepository.findByCategory(category);
        }
        return courseRepository.findAllByOrderByStudentCountDesc();
    }

    public Course getCourseDetail(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在"));
    }

    @Transactional
    public void updateProgress(Long userId, Long courseId, Integer currentChapter, Integer progressPct) {
        UserCourseProgressId id = new UserCourseProgressId(userId, courseId);
        UserCourseProgress progress = progressRepository.findById(id)
                .orElse(new UserCourseProgress());
        progress.setId(id);
        progress.setCurrentChapter(currentChapter);
        progress.setProgressPct(progressPct);
        progress.setLastWatchedAt(LocalDateTime.now());
        progressRepository.save(progress);
    }
}

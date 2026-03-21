package com.xuexi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_course_progress")
public class UserCourseProgress {
    @EmbeddedId
    private UserCourseProgressId id;

    @Column(name = "current_chapter", nullable = false)
    private Integer currentChapter = 1;

    @Column(name = "progress_pct", nullable = false)
    private Integer progressPct = 0;

    @Column(name = "last_watched_at")
    private LocalDateTime lastWatchedAt;
}

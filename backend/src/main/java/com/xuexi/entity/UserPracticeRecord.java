package com.xuexi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_practice_records")
public class UserPracticeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "practice_date", nullable = false)
    private LocalDate practiceDate;

    @Column(nullable = false)
    private Integer score = 0;

    @Column(name = "time_sec", nullable = false)
    private Integer timeSec = 0;

    @Column(name = "correct_count", nullable = false)
    private Integer correctCount = 0;

    @Column(nullable = false)
    private Integer total = 10;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

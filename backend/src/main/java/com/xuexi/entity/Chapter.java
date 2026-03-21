package com.xuexi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "duration_sec", nullable = false)
    private Integer durationSec = 0;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "order_num", nullable = false)
    private Integer orderNum = 0;
}

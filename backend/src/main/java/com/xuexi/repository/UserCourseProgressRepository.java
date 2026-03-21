package com.xuexi.repository;

import com.xuexi.entity.UserCourseProgress;
import com.xuexi.entity.UserCourseProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, UserCourseProgressId> {
    Optional<UserCourseProgress> findById(UserCourseProgressId id);
}

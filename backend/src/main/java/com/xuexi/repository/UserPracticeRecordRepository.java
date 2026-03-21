package com.xuexi.repository;

import com.xuexi.entity.UserPracticeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface UserPracticeRecordRepository extends JpaRepository<UserPracticeRecord, Long> {

    @Query(value = "SELECT r.user_id AS userId, u.nickname AS nickname, u.avatar_url AS avatarUrl, MAX(r.score) AS score " +
            "FROM user_practice_records r JOIN users u ON r.user_id = u.id " +
            "WHERE r.practice_date = :today " +
            "GROUP BY r.user_id, u.nickname, u.avatar_url " +
            "ORDER BY score DESC LIMIT 10", nativeQuery = true)
    List<LeaderboardRow> findTodayLeaderboard(@Param("today") LocalDate today);

    interface LeaderboardRow {
        Long getUserId();
        String getNickname();
        String getAvatarUrl();
        Integer getScore();
    }
}

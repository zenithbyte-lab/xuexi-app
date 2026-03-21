package com.xuexi.repository;

import com.xuexi.entity.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    Optional<Checkin> findByUserIdAndCheckinDate(Long userId, LocalDate date);
    List<Checkin> findByUserIdAndCheckinDateBetweenOrderByCheckinDateAsc(Long userId, LocalDate start, LocalDate end);
    Optional<Checkin> findTopByUserIdOrderByCheckinDateDesc(Long userId);
}

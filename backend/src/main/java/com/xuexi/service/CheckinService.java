package com.xuexi.service;

import com.xuexi.dto.CheckinStatsDto;
import com.xuexi.entity.Checkin;
import com.xuexi.repository.CheckinRepository;
import com.xuexi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckinService {

    private final CheckinRepository checkinRepository;
    private final UserRepository userRepository;

    @Transactional
    public Checkin doCheckin(Long userId) {
        LocalDate today = LocalDate.now();
        Optional<Checkin> existing = checkinRepository.findByUserIdAndCheckinDate(userId, today);
        if (existing.isPresent()) {
            return existing.get();
        }

        int streakDays = 1;
        Optional<Checkin> yesterday = checkinRepository.findByUserIdAndCheckinDate(userId, today.minusDays(1));
        if (yesterday.isPresent()) {
            streakDays = yesterday.get().getStreakDays() + 1;
        }

        Checkin checkin = new Checkin();
        checkin.setUserId(userId);
        checkin.setCheckinDate(today);
        checkin.setStreakDays(streakDays);

        // Award points
        userRepository.findById(userId).ifPresent(user -> {
            user.setPoints(user.getPoints() + 10);
            userRepository.save(user);
        });

        return checkinRepository.save(checkin);
    }

    public CheckinStatsDto getStats(Long userId) {
        LocalDate today = LocalDate.now();
        boolean checkedToday = checkinRepository.findByUserIdAndCheckinDate(userId, today).isPresent();

        int streakDays = checkinRepository.findTopByUserIdOrderByCheckinDateDesc(userId)
                .map(c -> {
                    if (c.getCheckinDate().isEqual(today) || c.getCheckinDate().isEqual(today.minusDays(1))) {
                        return c.getStreakDays();
                    }
                    return 0;
                })
                .orElse(0);

        YearMonth month = YearMonth.of(today.getYear(), today.getMonthValue());
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        List<Checkin> monthCheckins = checkinRepository
                .findByUserIdAndCheckinDateBetweenOrderByCheckinDateAsc(userId, start, end);
        List<String> calendar = monthCheckins.stream()
                .map(c -> c.getCheckinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .collect(Collectors.toList());

        CheckinStatsDto dto = new CheckinStatsDto();
        dto.setStreakDays(streakDays);
        dto.setCheckedToday(checkedToday);
        dto.setMonthCalendar(calendar);
        return dto;
    }
}

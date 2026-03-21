package com.xuexi.service;

import com.xuexi.dto.UserProfileDto;
import com.xuexi.entity.User;
import com.xuexi.repository.CheckinRepository;
import com.xuexi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CheckinRepository checkinRepository;

    public UserProfileDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        int streakDays = checkinRepository.findTopByUserIdOrderByCheckinDateDesc(userId)
                .map(c -> {
                    if (c.getCheckinDate().isEqual(LocalDate.now()) ||
                        c.getCheckinDate().isEqual(LocalDate.now().minusDays(1))) {
                        return c.getStreakDays();
                    }
                    return 0;
                })
                .orElse(0);

        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setNickname(user.getNickname());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setPoints(user.getPoints());
        dto.setCompletedCourses(user.getCompletedCourses());
        dto.setStreakDays(streakDays);
        return dto;
    }

    public UserProfileDto updateProfile(Long userId, UserProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (dto.getNickname() != null) user.setNickname(dto.getNickname());
        if (dto.getAvatarUrl() != null) user.setAvatarUrl(dto.getAvatarUrl());
        userRepository.save(user);
        return getProfile(userId);
    }
}

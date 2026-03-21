package com.xuexi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuexi.dto.LeaderboardDto;
import com.xuexi.dto.PracticeResultDto;
import com.xuexi.entity.QuizQuestion;
import com.xuexi.entity.UserPracticeRecord;
import com.xuexi.repository.QuizQuestionRepository;
import com.xuexi.repository.UserPracticeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PracticeService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final UserPracticeRecordRepository practiceRecordRepository;
    private final ObjectMapper objectMapper;

    public List<QuizQuestion> getTodayQuestions() {
        return quizQuestionRepository.findRandomQuestions(10);
    }

    public PracticeResultDto submitAnswers(Long userId, Map<String, String> answers) {
        List<QuizQuestion> all = quizQuestionRepository.findAll();
        int correctCount = 0;
        int total = answers.size();

        for (Map.Entry<String, String> entry : answers.entrySet()) {
            Long questionId = Long.parseLong(entry.getKey());
            String userAnswer = entry.getValue();
            for (QuizQuestion q : all) {
                if (q.getId().equals(questionId) && q.getAnswer().equalsIgnoreCase(userAnswer)) {
                    correctCount++;
                    break;
                }
            }
        }

        PracticeResultDto result = new PracticeResultDto();
        result.setCorrectCount(correctCount);
        result.setTotal(total);
        result.setScore(total > 0 ? correctCount * 100 / total : 0);
        result.setTimeSec(0);

        // Save practice record
        UserPracticeRecord record = new UserPracticeRecord();
        record.setUserId(userId);
        record.setPracticeDate(LocalDate.now());
        record.setScore(result.getScore());
        record.setTimeSec(result.getTimeSec());
        record.setCorrectCount(correctCount);
        record.setTotal(total);
        practiceRecordRepository.save(record);

        return result;
    }

    public List<LeaderboardDto> getLeaderboard(Long currentUserId) {
        List<UserPracticeRecordRepository.LeaderboardRow> rows =
                practiceRecordRepository.findTodayLeaderboard(LocalDate.now());
        List<LeaderboardDto> list = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            UserPracticeRecordRepository.LeaderboardRow row = rows.get(i);
            LeaderboardDto dto = new LeaderboardDto();
            dto.setRank(i + 1);
            dto.setNickname(row.getNickname());
            dto.setAvatarUrl(row.getAvatarUrl());
            dto.setScore(row.getScore());
            dto.setIsMe(row.getUserId().equals(currentUserId));
            list.add(dto);
        }
        return list;
    }
}

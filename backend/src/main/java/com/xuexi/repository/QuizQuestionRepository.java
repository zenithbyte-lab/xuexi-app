package com.xuexi.repository;

import com.xuexi.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    @Query(value = "SELECT * FROM quiz_questions ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<QuizQuestion> findRandomQuestions(int count);
}

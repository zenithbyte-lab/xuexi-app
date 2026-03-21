package com.xuexi.service;

import com.xuexi.dto.MessageDto;
import com.xuexi.entity.Message;
import com.xuexi.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<MessageDto> getMessages(Long userId) {
        return messageRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public long getUnreadCount(Long userId) {
        return messageRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markAsRead(Long userId, Long messageId) {
        messageRepository.findById(messageId).ifPresent(msg -> {
            if (msg.getUserId().equals(userId)) {
                msg.setIsRead(true);
                messageRepository.save(msg);
            }
        });
    }

    private MessageDto toDto(Message msg) {
        MessageDto dto = new MessageDto();
        dto.setId(msg.getId());
        dto.setType(msg.getType());
        dto.setTitle(msg.getTitle());
        dto.setContent(msg.getContent());
        dto.setIcon(msg.getIcon());
        dto.setIsRead(msg.getIsRead());
        dto.setCreatedAt(msg.getCreatedAt());
        dto.setTimeAgo(timeAgo(msg.getCreatedAt()));
        return dto;
    }

    private String timeAgo(LocalDateTime time) {
        Duration d = Duration.between(time, LocalDateTime.now());
        if (d.toMinutes() < 1) return "刚刚";
        if (d.toHours() < 1) return d.toMinutes() + "分钟前";
        if (d.toDays() < 1) return d.toHours() + "小时前";
        return d.toDays() + "天前";
    }
}

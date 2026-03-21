package com.xuexi.service;

import com.xuexi.dto.CommentDto;
import com.xuexi.dto.PostDto;
import com.xuexi.dto.PostRequest;
import com.xuexi.entity.Comment;
import com.xuexi.entity.Post;
import com.xuexi.repository.CommentRepository;
import com.xuexi.repository.PostRepository;
import com.xuexi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public List<PostDto> getPosts(String type) {
        List<Post> posts = (type != null && !type.isEmpty())
                ? postRepository.findByTypeOrderByCreatedAtDesc(type)
                : postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public PostDto createPost(Long userId, PostRequest req) {
        Post post = new Post();
        post.setUserId(userId);
        post.setType(req.getType());
        post.setContent(req.getContent());
        post.setImageUrl(req.getImageUrl());
        post = postRepository.save(post);
        return toDto(post);
    }

    @Transactional
    public void toggleLike(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("帖子不存在"));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    public List<CommentDto> getComments(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream().map(this::toCommentDto).collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addComment(Long userId, Long postId, String content) {
        Comment c = new Comment();
        c.setPostId(postId);
        c.setUserId(userId);
        c.setContent(content);
        c = commentRepository.save(c);

        postRepository.findById(postId).ifPresent(p -> {
            p.setCommentCount(p.getCommentCount() + 1);
            postRepository.save(p);
        });

        return toCommentDto(c);
    }

    private PostDto toDto(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setType(post.getType());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setLiked(false);
        dto.setCreatedAt(post.getCreatedAt());
        dto.setTimeAgo(timeAgo(post.getCreatedAt()));

        userRepository.findById(post.getUserId()).ifPresent(u -> {
            dto.setNickname(u.getNickname());
            dto.setAvatarUrl(u.getAvatarUrl());
        });

        return dto;
    }

    private CommentDto toCommentDto(Comment c) {
        CommentDto dto = new CommentDto();
        dto.setId(c.getId());
        dto.setUserId(c.getUserId());
        dto.setContent(c.getContent());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setTimeAgo(timeAgo(c.getCreatedAt()));
        userRepository.findById(c.getUserId()).ifPresent(u -> {
            dto.setNickname(u.getNickname());
            dto.setAvatarUrl(u.getAvatarUrl());
        });
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

package com.sparta.instahub.comment.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.service.UserService;
import com.sparta.instahub.comment.Repository.CommentRepository;
import com.sparta.instahub.comment.dto.CommentRequestDto;
import com.sparta.instahub.comment.dto.CommentResponseDto;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.like.entity.CommentLike;
import com.sparta.instahub.like.repository.CommentLikeRepository;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentLikeRepository commentLikeRepository;

    //댓글 생성
    public String createComment(Long postId, CommentRequestDto requestDto, String username) {
        Post post = findPostById(postId);
        User user = userService.getUserByName(username);

        Comment comment = new Comment(requestDto, post, user);
        commentRepository.save(comment);
        return "create comment";
    }

    //댓글 조회
    public List<CommentResponseDto> getAllComment(Long postId) {
        Post post = findPostById(postId);
        List<Comment> commentList = post.getComments;
        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        for (Comment responseDto : commentList) {
            responseDtoList.add(new CommentResponseDto(responseDto));
        }
        return responseDtoList;
    }

    //내가 좋아요 한 댓글 조회
    @Transactional(readOnly = true)
    public List<Comment> getMyLikeComment(String username, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC);
        Pageable pageable = PageRequest.of(page, 5, sort);

        User user = userService.getUserByName(username);
        Long userId = user.getId();

        List<CommentLike> commentLikes = commentLikeRepository.findByUserId(userId, pageable);

        return commentLikes.stream()
                .map(CommentLike::getComment)
                .collect(Collectors.toList());
    }

    //댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, String username) {
        Comment comment = findCommentById(commentId);
        User user = userService.getUserByName(username);
        if (comment.getUser().getId().equals(user.getId())) {
            comment.update(requestDto);
            return new CommentResponseDto(comment);
        } else {
            throw new IllegalArgumentException("ID가 일치하지 않습니다.");
        }
    }

    //댓글 삭제
    public ResponseEntity<String> deleteComment(Long id, String username) {
        Comment comment = findCommentById(id);
        User user = userService.getUserByName(username);
        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.delete(comment);
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } else throw new IllegalArgumentException("ID가 일치하지 않습니다.");

    }

    // id 존재 확인 메서드
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
    }

    //게시물 id 존재 확인
    private Post findPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

    }

}

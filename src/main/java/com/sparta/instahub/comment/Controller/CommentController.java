package com.sparta.instahub.comment.Controller;

import com.sparta.instahub.comment.dto.CommentRequestDto;
import com.sparta.instahub.comment.dto.CommentResponseDto;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping
    public ResponseEntity<String> createComment(@PathVariable Long postId,
                                                @RequestBody CommentRequestDto requestDto,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        String result = commentService.createComment(postId, requestDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //댓글 조회
    @GetMapping
    public List<CommentResponseDto> getAllComment(@PathVariable Long postId) {
        return commentService.getAllComment(postId);
    }

    //내가 좋아요 한 댓글 조회
    @GetMapping("/mylike")
    public ResponseEntity<List<CommentResponseDto>> getMyLikeComment(@AuthenticationPrincipal UserDetails userDetails,
                                                                     @RequestParam(value = "page") int page) {
        List<Comment> comments = commentService.getMyLikeComment(userDetails.getUsername(), page - 1);
        List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(comment -> new CommentResponseDto(comment.getId(), comment.getContents()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentResponseDtos, HttpStatus.OK);
    }

    //댓글 수정
    @PutMapping("/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long postId,
                                            @PathVariable Long commentId,
                                            @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.updateComment(postId, requestDto, userDetails.getUsername());

    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetails userDetails) {
        return commentService.deleteComment(commentId, userDetails.getUsername());
    }

}

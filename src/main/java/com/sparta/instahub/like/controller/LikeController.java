package com.sparta.instahub.like.controller;

import com.sparta.instahub.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 게시글 좋아요
    @PostMapping("/api/post/{postId}/like")
    public ResponseEntity<String> addPostLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        String result = likeService.addPostLike(postId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/api/post/{postId}/like")
    public ResponseEntity<String> removePostLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        String result = likeService.removePostLike(postId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //댓글 좋아요
    @PostMapping("/api/comment/{commentId}/like")
    public ResponseEntity<String> addCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        String result = likeService.addCommentLike(commentId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/api/comment/{commentId}/like")
    public ResponseEntity<String> removeCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        likeService.removeCommentLike(commentId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 좋아요 취소");
    }
}

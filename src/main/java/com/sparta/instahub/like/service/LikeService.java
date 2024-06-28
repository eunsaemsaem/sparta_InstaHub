package com.sparta.instahub.like.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface LikeService {

    // 게시글 좋아요
    String addPostLike(Long postId, String username);

    // 게시글 좋아요 취소
    String removePostLike(Long postId, UserDetails userDetails);

    // 댓글 좋아요
    String addCommentLike(Long commentId, String username);

    // 댓글 좋아요 취소
    ResponseEntity<String> removeCommentLike(Long commentId, UserDetails userDetails);
}

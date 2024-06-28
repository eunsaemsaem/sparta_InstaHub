package com.sparta.instahub.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    @Override
    public ResponseEntity<String> addPostLike(Long postId, UserDetails userDetails) {
        return null;
    }

    @Override
    public ResponseEntity<String> removePostLike(Long postId, UserDetails userDetails) {
        return null;

    }

    @Override
    public ResponseEntity<String> addCommentLike(Long commentId, UserDetails userDetails) {
        return null;

    }

    @Override
    public ResponseEntity<String> removeCommentLike(Long commentId, UserDetails userDetails) {
        return null;

    }


}

package com.sparta.instahub.like.repository;

import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.like.entity.CommentLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByComment(Comment comment);

    List<CommentLike> findByUserId(Long userId, Pageable pageable);
}

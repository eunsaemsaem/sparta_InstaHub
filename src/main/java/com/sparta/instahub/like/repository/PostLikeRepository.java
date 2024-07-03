package com.sparta.instahub.like.repository;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.like.entity.PostLike;
import com.sparta.instahub.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<Object> findByUserAndPost(User user, Post post);

    PostLike findByPost(Post post);

    Iterable<? extends Long> findByUser(User user);

    List<PostLike> findByUserId(Long userId, Pageable pageable);
}

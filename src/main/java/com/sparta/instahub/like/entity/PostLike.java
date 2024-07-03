package com.sparta.instahub.like.entity;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.common.entity.BaseEntity;
import com.sparta.instahub.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table
@NoArgsConstructor
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostLike(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}

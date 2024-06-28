package com.sparta.instahub.post.entity;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {

    // 기본 키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // 게시물 제목
    @Column(nullable = false)
    private String title;

    // 게시물 내용
    @Column(nullable = false)
    private String content;

    // 이미지 URl
    @Column
    private String imageUrl;

    // 게시글의 좋아요 수
    @Column
    private Long postLike;

    //Comment Service 내 post.getComments 관련 - 게시물 삭제될 경우 게시물 내 모든 댓글 삭제
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Comment> getComments;

    // 빌더 패턴을 사용한 생성자
//    @Builder
    public Post(User user, String title, String content, String imageUrl) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.postLike = 0L;
    }

    // 게시물 업데이트 메서드
    public void update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    // 좋아요 추가 메서드
    public void addLike() {
        if (this.postLike == null) {
            this.postLike = 0L;
        }
        this.postLike = postLike + 1L;
    }

    // 좋아요 삭제 메서드
    public void removeLike() {
        this.postLike = postLike - 1L;
    }
}

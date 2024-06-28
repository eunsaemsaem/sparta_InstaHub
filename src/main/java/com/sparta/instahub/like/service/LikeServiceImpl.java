package com.sparta.instahub.like.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.service.UserServiceImpl;
import com.sparta.instahub.like.entity.PostLike;
import com.sparta.instahub.like.repository.PostLikeRepository;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final UserServiceImpl userServiceImpl;
    private final PostServiceImpl postServiceImpl;
    private final PostLikeRepository postLikeRepository;

    @Override
    public String addPostLike(Long postId, String username) {
        // user, post 찾기
        User user = userServiceImpl.getUserByName(username);
        Post post = postServiceImpl.getPostById(postId);

        // 자신이 작성한 글에 좋아요 불가능 (if문)
        if (user.getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("자신의 글에는 좋아요를 누를 수 없습니다.");
        }

        // 좋아요 추가
        PostLike postLike = new PostLike(user, post);

        // 이미 좋아요 누른 글인지 확인 (if문)
//        if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
//            throw new IllegalArgumentException("이미 좋아요를 누른 글입니다.");
//        }
        // 게시글에 addlike
        post.addLike();

        // repository 저장
        postLikeRepository.save(postLike);

        return "게시글 좋아요 +1";
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

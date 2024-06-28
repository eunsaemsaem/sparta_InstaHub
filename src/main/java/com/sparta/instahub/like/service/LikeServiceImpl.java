package com.sparta.instahub.like.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.service.UserServiceImpl;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.comment.service.CommentService;
import com.sparta.instahub.like.entity.CommentLike;
import com.sparta.instahub.like.entity.PostLike;
import com.sparta.instahub.like.repository.CommentLikeRepository;
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
    private final CommentService commentService;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

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
    public String removePostLike(Long postId, UserDetails userDetails) {
        // postLike 찾기
        Post post = postServiceImpl.getPostById(postId);
        PostLike postLike = postLikeRepository.findByPost(post);

        // 본인 확인
        if (!(userDetails.getUsername().equals(postLike.getUser().getUsername()))) {
            throw new IllegalArgumentException("다른 사람의 좋아요를 삭제할 수 없습니다.");
        }

        // 디비 삭제, postLike -1
        postLikeRepository.delete(postLike);
        post.removeLike();

        return "게시글 좋아요 취소";
    }

    @Override
    public String addCommentLike(Long commentId, String username) {
        // user, post 찾기
        User user = userServiceImpl.getUserByName(username);
        Comment comment = commentService.findCommentById(commentId);

        // 자신이 작성한 글에 좋아요 불가능 (if문)
        if (user.getId().equals(comment.getUser().getId())) {
            throw new IllegalArgumentException("자신의 글에는 좋아요를 누를 수 없습니다.");
        }

        // 좋아요 추가
        CommentLike commentLike = new CommentLike(user, comment);

        // 이미 좋아요 누른 글인지 확인 (if문)
//        if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
//            throw new IllegalArgumentException("이미 좋아요를 누른 글입니다.");
//        }
        // 게시글에 addlike
        comment.addLike();

        // repository 저장
        commentLikeRepository.save(commentLike);

        return "댓글 좋아요 +1";
    }

    @Override
    public ResponseEntity<String> removeCommentLike(Long commentId, UserDetails userDetails) {
        return null;

    }


}

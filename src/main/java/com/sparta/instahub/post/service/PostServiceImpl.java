package com.sparta.instahub.post.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.entity.UserRole;
import com.sparta.instahub.auth.service.UserServiceImpl;
import com.sparta.instahub.exception.InaccessiblePostException;
import com.sparta.instahub.exception.UnauthorizedException;
import com.sparta.instahub.like.entity.PostLike;
import com.sparta.instahub.like.repository.PostLikeRepository;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.repository.PostRepository;
import com.sparta.instahub.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserServiceImpl userService;
    private final S3Service s3Service;
    private final PostLikeRepository postLikeRepository;

    /**
     * 새 게시물 생성 */
    @Override
    @Transactional
    public Post createPost(String title, String content, MultipartFile image, String username) {
        try {
            // 현재 로그인된 사용자 가져오기
            User user = userService.getUserByName(username);

            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = s3Service.uploadFile(image);
            }
            Post post = new Post(user, title, content, imageUrl);

            return postRepository.save(post); // Post 객체 저장
        } catch (InaccessiblePostException e) {
            throw new InaccessiblePostException("포스트를 생성할 수 없습니다.");
        }
    }

    /**
     * 게시물 조회 */
    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // ID로 게시물 조회
    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new InaccessiblePostException("Invalid post ID"));
    }

    // 내가 좋아요 한 게시물 조회
    @Override
    @Transactional(readOnly = true)
    public List<Post> getMyLikePost(String username) {
        User user = userService.getUserByName(username);
        Long userId = user.getId();

        // userId로 모든 PostLike 엔티티를 가져옴
        List<PostLike> postLikes = postLikeRepository.findByUserId(userId);

        // 각 PostLike 엔티티에서 Post 객체를 추출하여 리스트로 반환
        return postLikes.stream()
                .map(PostLike::getPost)
                .collect(Collectors.toList());
    }

    /**
     * 게시물 수정 */
    @Override
    @Transactional
    public Post updatePost(Long id, String title, String content, MultipartFile image, String username) {
        try {
            // 현재 로그인된 사용자 가져오기
            User currentUser = userService.getUserByName(username);
            Post post = getPostById(id); // ID로 게시물 조회

            if (!post.getUser().equals(currentUser)) {
                throw new UnauthorizedException("포스트 수정 권한이 없는 사용자 입니다.");
            }
            if (image != null && !image.isEmpty()) {
                if (post.getImageUrl() != null) {
                    s3Service.deleteFile(post.getImageUrl());
                }
                String imageUrl = s3Service.uploadFile(image);
                post.update(title, content, imageUrl);
            } else {
                post.update(title, content, post.getImageUrl());
            }
            return postRepository.save(post);
        } catch (InaccessiblePostException e) {
            throw new InaccessiblePostException("포스트를 수정할 수 없습니다.");
        }
    }

    /**
     * 게시물 삭제 */
    @Override
    @Transactional
    public void deletePost(Long id, String username) {
        User currentUser = userService.getUserByName(username);
        Post post = getPostById(id); // ID로 게시물 조회

        // 현재 로그인된 사용자가 게시글 작성자이거나 관리자인지 확인
        if (!post.getUser().equals(currentUser) && currentUser.getUserRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("포스트 삭제 권한이 없는 사용자 입니다.");
        }
        if (post.getImageUrl() != null) {
            s3Service.deleteFile(post.getImageUrl());
        }
        postRepository.deleteById(id); // ID로 게시물 삭제
    }

    // 모든 게시물 삭제
    @Override
    @Transactional
    public void deleteAllPosts() {
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            if (post.getImageUrl() != null) {
                s3Service.deleteFile(post.getImageUrl());
            }
        }
        postRepository.deleteAll();
    }
}

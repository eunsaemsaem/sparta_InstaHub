package com.sparta.instahub.post.service;

import com.sparta.instahub.post.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Post 엔티티에 대한 비즈니스 로직을 처리하는 서비스 클래스

public interface PostService {

    /**
     * 새 게시물 생성 */
    Post createPost(String title, String content, MultipartFile imageUrl, String username);

    /**
     * 게시물 조회 */
    List<Post> getAllPosts();

    // ID로 게시물 조회
    Post getPostById(Long id);

    // 내가 좋아요 한 게시물 조회
    List<Post> getMyLikePost(String username, int page, String sortBy);

    /**
     * 게시물 수정 */
    Post updatePost(Long id, String title, String content, MultipartFile imageUrl, String username);

    /**
     * 게시물 삭제 */
    void deletePost(Long id, String username);

    // 모든 게시물 삭제
    void deleteAllPosts();

}

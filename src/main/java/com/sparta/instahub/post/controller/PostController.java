package com.sparta.instahub.post.controller;

import com.sparta.instahub.post.dto.PostRequestDto;
import com.sparta.instahub.post.dto.PostResponseDto;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// 게시물 관련 요청을 처리하는 REST 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    /**
     * 게시물 생성
     *
     * @param postRequestDto
     * @param userDetails
     * @return
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@ModelAttribute PostRequestDto postRequestDto,
                                                      @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Post post = postService.createPost(postRequestDto.getTitle(), postRequestDto.getContent(), postRequestDto.getImage(), userDetails.getUsername());
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .imageUrl(post.getImageUrl())
                .build();
        return new ResponseEntity<>(postResponseDto, HttpStatus.CREATED);
    }

    /**
     * 게시물 조회
     *
     * @return
     */
    /* 전체 조회 */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(post -> PostResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .author(post.getUser().getUsername())
                        .imageUrl(post.getImageUrl())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    /* 단건 조회 */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .imageUrl(post.getImageUrl())
                .build();
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    /**
     * 게시물 수정
     *
     * @param id
     * @param postRequestDto
     * @param userDetails
     * @return
     * @throws IOException
     */
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
                                                      @ModelAttribute PostRequestDto postRequestDto,
                                                      @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        Post post = postService.updatePost(id, postRequestDto.getTitle(), postRequestDto.getContent(), postRequestDto.getImage(), userDetails.getUsername());
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .imageUrl(post.getImageUrl())
                .build();
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    /**
     * 게시물 삭제
     *
     * @param id
     * @param userDetails
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(id, userDetails.getUsername()); // 게시물 삭제
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

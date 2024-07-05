package com.sparta.instahub.profile.controller;


import com.sparta.instahub.profile.dto.PasswordRequestDto;
import com.sparta.instahub.profile.dto.PasswordResponseDto;
import com.sparta.instahub.profile.dto.ProfileRequestDto;
import com.sparta.instahub.profile.dto.ProfileResponseDto;
import com.sparta.instahub.profile.entity.Profile;
import com.sparta.instahub.profile.service.ProfileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile (@AuthenticationPrincipal UserDetails userDetails) {
        Profile profile = profileService.getProfile(userDetails.getUsername());
        int postLike = profileService.getPostLike(userDetails.getUsername());
        int commentLike = profileService.getCommentLike(userDetails.getUsername());

        ProfileResponseDto responseDto = new ProfileResponseDto(profile, postLike, commentLike);
        return ResponseEntity.ok().body(responseDto);
    }

    // 프로필 수정
    @PatchMapping("/{Id}")
    public ResponseEntity<ProfileResponseDto> updateProfile(@PathVariable Long Id,
                                                            @RequestBody ProfileRequestDto requestDto) {

        Profile updatedProfile = profileService.updateProfile(Id, requestDto);
        ProfileResponseDto responseDto = new ProfileResponseDto(
                updatedProfile.getEmail(),
                updatedProfile.getAddress(),
                updatedProfile.getIntroduction(),
                "수정이 성공적으로되었습니다."
        );
        return ResponseEntity.ok(responseDto);
    }

    // 비밀번호 수정
    @PatchMapping("/password")
    public ResponseEntity<PasswordResponseDto> updatePassword(@RequestBody PasswordRequestDto requestDto) throws BadRequestException {

        return ResponseEntity.ok(profileService.updatePassword(requestDto));
    }
}

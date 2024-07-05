package com.sparta.instahub.profile.dto;

import com.sparta.instahub.profile.entity.Profile;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String email;
    private String address;
    private String introduction;
    private String message;
    private int postLike;
    private int commentLike;

    public ProfileResponseDto(String email, String address, String introduction, String message) {
        this.email = email;
        this.address = address;
        this.introduction = introduction;
        this.message = message;
    }

    public ProfileResponseDto(String email, String address, String introduction, String message, int postLike, int commentLike) {
        this.email = email;
        this.address = address;
        this.introduction = introduction;
        this.message = message;
        this.postLike = postLike;
        this.commentLike = commentLike;
    }

    public ProfileResponseDto(Profile profile, int postLike, int commentLike) {
        this.email = profile.getEmail();
        this.address = profile.getAddress();
        this.introduction = profile.getIntroduction();
        this.postLike = postLike;
        this.commentLike = commentLike;
    }
}

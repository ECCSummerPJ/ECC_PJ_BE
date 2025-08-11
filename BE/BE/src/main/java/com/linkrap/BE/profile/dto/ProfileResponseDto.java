package com.linkrap.BE.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {
    private String email;
    private String nickname;
    private String profileImage;   // ex) "data:image/png;base64,...." / 이미지 없으면 null
}

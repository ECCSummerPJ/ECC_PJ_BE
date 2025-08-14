package com.linkrap.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileDto {
    private String email;
    private String nickname;
    private String profileImage;   // Url 그대로
}

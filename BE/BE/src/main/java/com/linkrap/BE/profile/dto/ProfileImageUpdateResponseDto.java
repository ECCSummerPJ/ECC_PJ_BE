package com.linkrap.BE.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileImageUpdateResponseDto {
    private boolean hasUpdated;   // 저장 성공 여부
    private String profileImageBase64; // 방금 저장한 이미지를 Base64로(없으면 null)
}
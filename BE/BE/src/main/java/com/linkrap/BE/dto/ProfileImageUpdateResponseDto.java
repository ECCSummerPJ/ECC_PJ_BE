package com.linkrap.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileImageUpdateResponseDto {
    private boolean hasUpdated;   // 저장 성공 여부
    private String profileImageUrl;
}
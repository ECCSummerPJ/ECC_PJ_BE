package com.linkrap.BE.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateResponseDto {
    private Map<String, Boolean> hasUpdated; // 닉네임/이메일/비밀번호 변경 여부
    private ProfileResponseDto profile;     // 변경 후 프로필 정보
}
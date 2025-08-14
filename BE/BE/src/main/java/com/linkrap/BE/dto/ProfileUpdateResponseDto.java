package com.linkrap.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateResponseDto {
    private Map<String, Boolean> hasUpdated; // 닉네임/이메일/비밀번호 변경 여부
    private ProfileDto profile;     // 변경 후 프로필 정보
}
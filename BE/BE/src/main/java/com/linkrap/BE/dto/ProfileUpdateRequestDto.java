package com.linkrap.BE.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequestDto {
    private String nickname;         // 변경할 닉네임(선택)
    private String email;            // 변경할 이메일(선택)
    private String newPassword;      // 비번 변경 시 새 비번(선택)
}

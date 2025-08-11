package com.linkrap.BE.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileUpdateResponseDto {
    private List<String> updatedFields; // ["nickname","email","password"]
    private String email;               // 최종 이메일
    private String nickname;            // 최종 닉네임
}

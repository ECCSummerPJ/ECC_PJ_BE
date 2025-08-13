package com.linkrap.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long id;
    private String userId;
    private String email;
    private String nickname;
    private String profileImageUrl;
}

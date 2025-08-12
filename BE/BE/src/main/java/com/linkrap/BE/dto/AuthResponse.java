package com.linkrap.BE.auth.dto;


import jakarta.persistence.Id;


public record AuthResponse {

    Long id;
    String userId;
    String email;
    String nickname;
    String profileImageUrl
) { }


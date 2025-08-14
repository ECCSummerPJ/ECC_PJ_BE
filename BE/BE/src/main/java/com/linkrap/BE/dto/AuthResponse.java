package com.linkrap.BE.dto;


import jakarta.persistence.Id;


public record AuthResponse (

    Integer userId,
    String loginId,
    String email,
    String nickname,
    String profileImageUrl
) { }


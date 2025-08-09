package com.linkrap.BE.profile.dto;


public record ProfileResponse(
        String email,
        String nickname,
        boolean hasProfileImage
) {}
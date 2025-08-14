package com.linkrap.BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Size(min=4, max=20) String userId,
        @NotBlank @Size(min=8, max=20) String password
) {}

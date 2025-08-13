package com.linkrap.BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {

    @NotBlank
    @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
    private String userId;

    @NotBlank
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    private String password;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

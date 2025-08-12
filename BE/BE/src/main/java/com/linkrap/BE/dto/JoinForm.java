package com.linkrap.BE.auth.dto;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoinForm {

    @NotBlank
    @Size(min = 3, max = 20)
    private String userId;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min =8, max = 20)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String passwordConfirm;

    @NotBlank
    @Size(min = 2, max = 15)
    private String nickname;

    @Size(max=500)
    private String profileImageUrl;

    public boolean passwordsMatch() {
        return password != null && password.equals(passwordConfirm);
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPasswordConfirm() { return passwordConfirm; }
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}

}

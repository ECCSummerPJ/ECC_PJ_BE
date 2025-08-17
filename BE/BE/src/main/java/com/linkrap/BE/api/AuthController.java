package com.linkrap.BE.api;

import com.linkrap.BE.dto.AuthResponse;
import com.linkrap.BE.dto.LoginRequest;
import com.linkrap.BE.dto.JoinForm;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        var result = authService.login(req); // LoginResult

        ResponseCookie cookie = ResponseCookie.from("refresh_token", result.tokens().refreshToken())
                .httpOnly(true)
                .secure(false)      // HTTPS면 true
                .path("/")
                .sameSite("Lax")    // 크로스 도메인이면 "None" + secure(true)
                .maxAge(14 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(result.body()); // 바디에는 accessToken만
    }

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse join(@Valid @RequestBody JoinForm form) {
        return authService.join(form);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshCookie
    ) {
        authService.revoke(refreshCookie, null);

        ResponseCookie expired = ResponseCookie.from("refresh_token", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        return ResponseEntity.noContent()
                .header("Set-Cookie", expired.toString())
                .build();
    }






    @Operation(summary = "회원 탈퇴(비밀번호 확인 필수)")
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMe(@AuthenticationPrincipal Users user,
                         @RequestParam(defaultValue = "false") boolean hard,
                         @RequestBody(required = false) DeleteConfirm req) {
        authService.deleteUser(user.getId(), hard, req != null ? req.getPassword() : null);
    }
    @Data
    class DeleteConfirm {
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }
}

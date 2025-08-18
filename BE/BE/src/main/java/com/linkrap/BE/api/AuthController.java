package com.linkrap.BE.api;

import com.linkrap.BE.dto.AuthResponse;
import com.linkrap.BE.dto.DeleteRequest;
import com.linkrap.BE.dto.LoginRequest;
import com.linkrap.BE.dto.JoinForm;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        var result = authService.login(req); // body + tokens(access, refresh)
        ResponseCookie refresh = ResponseCookie.from("refresh_token", result.tokens().refreshToken())
                .path("/")
                .httpOnly(true)
                .maxAge(14 * 24 * 3600)   // 14일
                .secure(false)          // HTTPS일 때만! (로컬 http면 주석/false)
                // .sameSite("Lax")       // SPA가 같은 도메인/포트면 Lax로 OK
                .build();

        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, refresh.toString())
                .body(result.body());
    }

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<AuthResponse> join(@RequestBody JoinForm form) {
        var res = authService.join(form);
        ResponseCookie refresh = ResponseCookie.from("refresh_token", /* 발급한 refresh */ "")
                .path("/")
                .httpOnly(true)
                .maxAge(14 * 24 * 3600)
                .build();
        return ResponseEntity.status(201)
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, refresh.toString())
                .body(res);
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
    public ResponseEntity<Void> deleteSelf(@RequestParam(defaultValue = "false") boolean hard,
                                           @Valid @RequestBody DeleteRequest body) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return ResponseEntity.status(401).build();

        Users me = (Users) auth.getPrincipal();        // JwtAuthFilter가 Users를 넣음
        authService.deleteUser(me.getUserId(), hard, body.password());
        return ResponseEntity.noContent().build();     // 성공 시 204
    }
}


package com.linkrap.BE.api;

import com.linkrap.BE.dto.AuthResponse;
import com.linkrap.BE.dto.JoinForm;
import com.linkrap.BE.dto.LoginRequest;
import com.linkrap.BE.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse join(@Valid @RequestBody JoinForm form) {
        return authService.join(form);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }
}

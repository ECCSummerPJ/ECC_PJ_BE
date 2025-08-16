package com.linkrap.BE.api;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(Exception e) {
        return Map.of("error", "이미 사용 중인 아이디 또는 이메일입니다.");
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(org.springframework.web.bind.MethodArgumentNotValidException e) {
        var field = e.getBindingResult().getFieldError();
        return Map.of("error", field != null ? field.getDefaultMessage() : "요청 형식이 올바르지 않습니다.");
    }
}
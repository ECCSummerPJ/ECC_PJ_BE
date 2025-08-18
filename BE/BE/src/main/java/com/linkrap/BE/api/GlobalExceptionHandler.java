package com.linkrap.BE.api;


import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<java.util.Map<String,String>> badBody(org.springframework.web.bind.MethodArgumentNotValidException e) {
        var m = new java.util.HashMap<String,String>();
        e.getBindingResult().getFieldErrors().forEach(er -> m.put(er.getField(), er.getDefaultMessage()));
        return ResponseEntity.badRequest().body(m); // 400
    }

    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
    public ResponseEntity<?> badCred(org.springframework.security.authentication.BadCredentialsException e) {
        return ResponseEntity.badRequest().body(java.util.Map.of("error","비밀번호 불일치")); // 400
    }

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    public ResponseEntity<?> notFound(jakarta.persistence.EntityNotFoundException e) {
        return ResponseEntity.status(404).body(java.util.Map.of("error","사용자 없음")); // 404
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<?> fk(org.springframework.dao.DataIntegrityViolationException e) {
        return ResponseEntity.status(409).body(java.util.Map.of("error","데이터 제약 위반")); // 409
    }
}

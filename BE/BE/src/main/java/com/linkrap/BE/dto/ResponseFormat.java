package com.linkrap.BE.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseFormat<T> {
    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    // Constructor without data
    public ResponseFormat(HttpStatus status, String message) {
        this(status, message, null);
    }

    // Constructor with data
    public ResponseFormat(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Static factory method without data
    public static <T> ResponseFormat<T> of(HttpStatus status, String message) {
        return new ResponseFormat<>(status, message);
    }

    // Static factory method with data
    public static <T> ResponseFormat<T> of(HttpStatus status, String message, T data) {
        return new ResponseFormat<>(status, message, data);
    }

    // 요청 정상 처리함
    public static <T> ResponseFormat<T> ok(String message, T data) {
        return ResponseFormat.of(HttpStatus.OK, message, data);
    }

    // post, put 정상 생성함
    public static <T> ResponseFormat<T> created(String message, T data) {
        return ResponseFormat.of(HttpStatus.CREATED, message, data);
    }

    // 잘못된 문법으로 요청을 보내고 있어 서버가 이해할 수 없음
    public static <T> ResponseFormat<T> failure(String message) {
        return ResponseFormat.of(HttpStatus.BAD_REQUEST, message);
    }

    // 요청한 URL 찾을 수 없음
    public static <T> ResponseFormat<T> notFound(String message) {
        return ResponseFormat.of(HttpStatus.NOT_FOUND, message);
    }

    // 요청은 정상적으로 들어왔으나 서버 문제로 응답할 수 없음
    public static <T> ResponseFormat<T> error(String message) {
        return ResponseFormat.of(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // Convenience method for creating a response from an existing data object
    public static <T> ResponseFormat<T> from(T data) {
        return ResponseFormat.of(HttpStatus.OK, "Operation successful", data);
    }
}
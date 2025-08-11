package com.linkrap.BE.profile.dto;

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

    // Convenience method for OK status with data
    public static <T> ResponseFormat<T> ok(String message, T data) {
        return ResponseFormat.of(HttpStatus.OK, message, data);
    }

    // Convenience method for creating a success response
    public static <T> ResponseFormat<T> success(String message, T data) {
        return ResponseFormat.of(HttpStatus.OK, message, data);
    }

    // Convenience method for creating a failure response
    public static <T> ResponseFormat<T> failure(String message) {
        return ResponseFormat.of(HttpStatus.BAD_REQUEST, message);
    }

    // Convenience method for creating an error response
    public static <T> ResponseFormat<T> error(String message) {
        return ResponseFormat.of(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // Convenience method for creating a response from an existing data object
    public static <T> ResponseFormat<T> from(T data) {
        return ResponseFormat.of(HttpStatus.OK, "Operation successful", data);
    }
}
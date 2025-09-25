package com.example.blog_assignment.dto;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ControllerExceptionDTO {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ControllerExceptionDTO(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

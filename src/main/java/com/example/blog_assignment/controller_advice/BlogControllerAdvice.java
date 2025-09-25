package com.example.blog_assignment.controller_advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.blog_assignment.dto.ControllerExceptionDTO;
import com.example.blog_assignment.exception.BlogNotFoundException;

@ControllerAdvice()
public class BlogControllerAdvice {

    @ExceptionHandler(BlogNotFoundException.class)
    public ResponseEntity<ControllerExceptionDTO> handleBlogNotFoundException(BlogNotFoundException ex) {
        ControllerExceptionDTO dto = new ControllerExceptionDTO(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ControllerExceptionDTO> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        ControllerExceptionDTO dto = new ControllerExceptionDTO(HttpStatus.BAD_REQUEST,
                "Data integrity violation :" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
    }
}

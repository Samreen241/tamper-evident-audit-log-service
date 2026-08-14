package com.samreen.auditlog.config;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(AuthenticationException.class)
  ResponseEntity<Map<String, String>> authenticationFailure() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("error", "invalid credentials"));
  }

  @ExceptionHandler({IllegalArgumentException.class})
  ResponseEntity<Map<String, String>> invalidRequest(IllegalArgumentException exception) {
    return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<Map<String, String>> duplicateOrConstraintFailure() {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("error", "duplicate or conflicting audit event"));
  }
}

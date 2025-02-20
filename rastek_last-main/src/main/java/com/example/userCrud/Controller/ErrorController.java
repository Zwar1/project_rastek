package com.example.userCrud.Controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import com.example.userCrud.Dto.web_response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<web_response<Map<String, String>>> constraintViolationException(ConstraintViolationException exception) {
        // Extract field names and error messages
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(web_response.<Map<String, String>>builder()
                        .error(errors)
                        .message("Error")
                        .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<web_response<Map<String, String>>> apiException(ResponseStatusException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("Error Description", exception.getReason() != null ? exception.getReason() : "Unknown error");

        return ResponseEntity.status(exception.getStatusCode())
                .body(web_response.<Map<String, String>>builder()
                        .error(error)
                        .message("Error")
                        .build());
    }
}

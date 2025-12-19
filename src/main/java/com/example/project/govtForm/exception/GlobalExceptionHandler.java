package com.example.project.govtForm.exception;

import com.example.project.govtForm.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // 1) HANDLE ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        // Build a custom error response
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now()) // Time of error
                .status(HttpStatus.NOT_FOUND.value()) // status code
                .error(HttpStatus.NOT_FOUND.name())   // NOT_FOUND
                .message(ex.getMessage()) // Message from thrown exception
                .path(request.getRequestURI()) // API endpoint that triggered error
                .build();

        // Return HTTP 404 with the error body
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Map to store field-specific errors
        Map<String, String> validationErrors = new HashMap<>();

        // Loop through each validation error
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError
                    ? ((FieldError) error).getField() // If error belongs to a field
                    : error.getObjectName(); // If not a field, then use object name
            String message = error.getDefaultMessage(); // The validation message
            validationErrors.put(fieldName, message); // Save it
        });

        // Build error response with validation details
        ErrorResponseDto apiError = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value()) // status code
                .error(HttpStatus.BAD_REQUEST.name())   // BAD_REQUEST
                .message("Validation failed")           // General validation error message
                .path(request.getRequestURI())
                .validationErrors(validationErrors)     // Add all validation issues
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    // 3) HANDLE BadCredentialsException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value()) // 401
                .error(HttpStatus.UNAUTHORIZED.name())
                .message("Invalid username or password") // Custom message
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 4) HANDLE UsernameNotFoundException and Thrown when user email/username does not exist in DB
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {
        ErrorResponseDto error = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value()) // 401
                .error(HttpStatus.UNAUTHORIZED.name())
                .message(ex.getMessage()) // Use the provided exception message
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();

        // Collect validation errors
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            validationErrors.put(fieldName, violation.getMessage());
        });

        // Build the response
        ErrorResponseDto apiError = ErrorResponseDto.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value()) // 400
                .error(HttpStatus.BAD_REQUEST.name())
                .message("Validation failed")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

}

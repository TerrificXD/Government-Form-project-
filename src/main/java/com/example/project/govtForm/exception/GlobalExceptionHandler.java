package com.example.project.govtForm.exception;

import com.example.project.govtForm.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // HANDLE ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        logger.warn("Resource not found at path {} - {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDto error = new ErrorResponseDto();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError(HttpStatus.NOT_FOUND.name());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    // HANDLE Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {

        logger.warn("Validation failed for request at path {}", request.getRequestURI());

        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = error instanceof FieldError
                    ? ((FieldError) error).getField()
                    : error.getObjectName();

            validationErrors.put(fieldName, error.getDefaultMessage());
        });

        ErrorResponseDto apiError = new ErrorResponseDto();
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setError(HttpStatus.BAD_REQUEST.name());
        apiError.setMessage("Validation failed");
        apiError.setPath(request.getRequestURI());
        apiError.setValidationErrors(validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


    // HANDLE BadCredentialsException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {

        logger.warn("Bad credentials attempt at path {}", request.getRequestURI());

        ErrorResponseDto error = new ErrorResponseDto();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setError(HttpStatus.UNAUTHORIZED.name());
        error.setMessage("Invalid username or password");
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


    // HANDLE UsernameNotFoundException
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {

        logger.warn("Username not found at path {} - {}", request.getRequestURI(), ex.getMessage());

        ErrorResponseDto error = new ErrorResponseDto();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setError(HttpStatus.UNAUTHORIZED.name());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


    // HANDLE ConstraintViolationException (Path variables / Request params)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {

        logger.warn("Constraint violation at path {}", request.getRequestURI());

        Map<String, String> validationErrors = new HashMap<>();

        ex.getConstraintViolations().forEach(v ->
                validationErrors.put(v.getPropertyPath().toString(), v.getMessage())
        );

        ErrorResponseDto apiError = new ErrorResponseDto();
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setError(HttpStatus.BAD_REQUEST.name());
        apiError.setMessage("Validation failed");
        apiError.setPath(request.getRequestURI());
        apiError.setValidationErrors(validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


    // Catch all exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralException(Exception ex, HttpServletRequest request){
        logger.warn("Unexpected error at path {}", request.getRequestURI());

        ErrorResponseDto apiError = new ErrorResponseDto();
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setError(HttpStatus.INTERNAL_SERVER_ERROR.name());
        apiError.setMessage("An unexpected error occurred");
        apiError.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

}

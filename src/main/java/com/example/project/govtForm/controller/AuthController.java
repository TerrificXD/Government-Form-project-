package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.JwtResponseDto;
import com.example.project.govtForm.dto.LoginRequest;
import com.example.project.govtForm.dto.SignupRequest;
import com.example.project.govtForm.service.IAuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final IAuthService iAuthService;

    public AuthController(IAuthService iAuthService) {
        this.iAuthService = iAuthService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        logger.info("Signup request received for email: {}", request.getEmail());
        ResponseEntity<?> response = iAuthService.signup(request);
        logger.info("Signup completed for email: {}", request.getEmail());
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login attempt for username/email: {}", request.getUsername());
        ResponseEntity<JwtResponseDto> response = iAuthService.login(request);
        logger.info("Login successful for username/email: {}", request.getUsername());
        return response;
    }
}

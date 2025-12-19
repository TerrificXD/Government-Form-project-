package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.JwtResponseDto;
import com.example.project.govtForm.dto.LoginRequest;
import com.example.project.govtForm.dto.SignupRequest;
import com.example.project.govtForm.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService iAuthService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {  // Validates input & maps JSON → SignupRequest DTO
        return iAuthService.signup(request); // Delegate the registration logic to the service layer
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequest request){   // Validate and map login request JSON
        return iAuthService.login(request); // Call login method and return JWT token with user details
    }
}

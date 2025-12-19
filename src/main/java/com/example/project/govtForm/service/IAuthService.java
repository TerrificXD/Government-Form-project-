package com.example.project.govtForm.service;

import com.example.project.govtForm.dto.JwtResponseDto;
import com.example.project.govtForm.dto.LoginRequest;
import com.example.project.govtForm.dto.SignupRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    ResponseEntity<?> signup(SignupRequest request);
    ResponseEntity<JwtResponseDto> login(LoginRequest request);
}

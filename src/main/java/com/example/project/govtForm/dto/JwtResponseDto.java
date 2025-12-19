package com.example.project.govtForm.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDto {
    private String token;
    private String tokenType;
    private String username;
    private List<String> roles;
    private String errorMessage;  // Added for error responses
}
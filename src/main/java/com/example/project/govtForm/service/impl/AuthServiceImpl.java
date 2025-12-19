package com.example.project.govtForm.service.impl;

import com.example.project.govtForm.dto.JwtResponseDto;
import com.example.project.govtForm.dto.LoginRequest;
import com.example.project.govtForm.dto.SignupRequest;
import com.example.project.govtForm.security.entity.SystemAdmin;
import com.example.project.govtForm.security.jwt.JwtUtils;
import com.example.project.govtForm.security.repository.SystemAdminRepository;
import com.example.project.govtForm.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final SystemAdminRepository systemAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CompromisedPasswordChecker compromisedPasswordChecker;


    // REGISTER ADMIN
    @Override
    public ResponseEntity<?> signup(SignupRequest request) {

        // Check if password and confirm password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("confirmPassword", "Password and Confirm Password do not match"));
        }

        // Check if the password is strong or not
        var checkResult = compromisedPasswordChecker.check(request.getPassword());
        if (checkResult.isCompromised()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("password", "Please choose a stronger password."));
        }

        // Create a map to store validation errors
        Map<String, String> errors = new HashMap<>();

        // Check existing username
        if (systemAdminRepository.existsByUsername(request.getUsername())) {
            errors.put("username", "Username is already registered");
        }

        // Check existing email
        if (systemAdminRepository.existsByEmail(request.getEmail())) {
            errors.put("email", "Email is already registered");
        }

        // Check existing phone number
        if (systemAdminRepository.existsByPhone(request.getPhone())) {
            errors.put("phone", "Phone number is already registered");
        }

        // If any validation errors exist, return them together
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // Create new SystemAdmin entity and hash the password
        SystemAdmin admin = SystemAdmin.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))  // Hash the password
                .build();

        // Save admin to database
        systemAdminRepository.save(admin);

        // Return response with success message
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "System admin registered successfully",
                        "username", admin.getUsername(),
                        "email", admin.getEmail()
                ));
    }


    // LOGIN
    @Override
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate username and password with AuthenticationManager and then authenticationManager will validate these credentials against the database
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),  // User-entered username
                            request.getPassword()   // User-entered password
                    )
            );

            // Extract authenticated user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Extract roles from authorities (ROLE_ADMIN)
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Generate JWT token using user details
            String token = jwtUtils.generateToken(userDetails);

            // Build the success JWT response DTO
            JwtResponseDto response = JwtResponseDto.builder()
                    .tokenType("Bearer")         // Token type convention
                    .token(token)                // JWT Token
                    .username(userDetails.getUsername())  // Authenticated username
                    .roles(roles)                 // User's roles/permissions
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            // Thrown when username or password is incorrect
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");

        } catch (AuthenticationException ex) {
            // Any other authentication failure
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed");

        } catch (Exception e) {
            // Catch any unexpected errors
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }


    // Helper method to build standardized error responses.
    private ResponseEntity<JwtResponseDto> buildErrorResponse(HttpStatus status, String message) {

        // Build an error DTO object with errorMessage
        JwtResponseDto errorResponse = JwtResponseDto.builder()
                .tokenType(null)        // No token on error
                .token(null)            // No token on error
                .username(null)         // No username on error
                .roles(null)            // No roles on error
                .errorMessage(message)  // Set the error message
                .build();

        // Return response with status
        return new ResponseEntity<>(errorResponse, status);
    }
}

package com.example.project.govtForm.service.impl;

import com.example.project.govtForm.dto.JwtResponseDto;
import com.example.project.govtForm.dto.LoginRequest;
import com.example.project.govtForm.dto.SignupRequest;
import com.example.project.govtForm.security.entity.User;
import com.example.project.govtForm.security.enums.Role;
import com.example.project.govtForm.security.jwt.JwtUtils;
import com.example.project.govtForm.security.repository.UserRepository;
import com.example.project.govtForm.service.IAuthService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            CompromisedPasswordChecker compromisedPasswordChecker
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.compromisedPasswordChecker = compromisedPasswordChecker;
    }

    // SIGNUP
    @Override
    public ResponseEntity<?> signup(SignupRequest request) {

        logger.info("Signup attempt for username: {} and email: {}", request.getUsername(), request.getEmail());

        //Password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            logger.warn("Signup failed: password confirmation mismatch for username: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("confirmPassword", "Password and Confirm Password do not match"));
        }

        //Compromised password check
        var checkResult = compromisedPasswordChecker.check(request.getPassword());
        if (checkResult.isCompromised()) {
            logger.warn("Signup failed: compromised password detected for username: {}",  request.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("password", "Please choose a stronger password."));
        }

        //Uniqueness validation
        Map<String, String> errors = new HashMap<>();

        if (userRepository.existsByUsername(request.getUsername())) {
            logger.warn("Signup failed: username already exists {}", request.getUsername());
            errors.put("username", "Username is already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Signup failed: Email already registered {}", request.getEmail());
            errors.put("email", "Email is already registered");
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        //ROLE ASSIGNMENT LOGIC
        boolean isFirstUser = userRepository.count() == 0;

        Set<Role> roles = isFirstUser
                ? Set.of(Role.ROLE_ADMIN, Role.ROLE_USER)
                : Set.of(Role.ROLE_USER);
        if (isFirstUser) {
            logger.info("First user signup detected (assigning ADMIN and USER role)");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);

        userRepository.save(user);

        logger.info("User registered successfully. username={}, role={}", user.getUsername(), user.getRoles());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", isFirstUser
                                ? "First user registered as ADMIN and USER"
                                : "User registered successfully",
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "roles", user.getRoles()
                )
        );
    }


    // LOGIN
    @Override
    public ResponseEntity<JwtResponseDto> login(LoginRequest request) {

        try {
            //Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            //Extract roles
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            //Generate JWT
            String token = jwtUtils.generateToken(
                    (org.springframework.security.core.userdetails.User) authentication.getPrincipal(),
                    null // employeeId will be added later if needed
            );

            logger.info("Login successful for user: {} with roles: {}", authentication.getName(), roles);

            JwtResponseDto response = new JwtResponseDto();
            response.setToken(token);
            response.setTokenType("Bearer");
            response.setUsername(authentication.getName());
            response.setRoles(roles);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            logger.warn("Login failed: bad credentials for user {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (AuthenticationException e) {
            logger.warn("Login failed due to authentication exception for user {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (Exception e) {
            logger.error("Unexpected error during login for user {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    public void promoteEmployeeToAdmin(Long employeeId) {
        logger.info("Promoting employee with id {} to ADMIN", employeeId);
        userRepository.promoteEmployeeUserToAdmin(employeeId);
        logger.info("Employee {} promoted to ADMIN successfully", employeeId);
    }
}

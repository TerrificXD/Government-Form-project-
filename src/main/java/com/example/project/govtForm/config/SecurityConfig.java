package com.example.project.govtForm.config;

import com.example.project.govtForm.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // MAIN SECURITY CONFIGURATION
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF configuration
                 .csrf(csrf -> csrf.disable())

//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())  // Store CSRF token inside cookie
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())  // extracting the token
//                )

                // JWT = Stateless authentication (Because JWT authentication does not use sessions)
                .sessionManagement(session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // No session will be created; JWT will manage authentication
                )

                // AUTHORIZE specific endpoints
                .authorizeHttpRequests(auth -> {
                    // Public endpoints (no authentication required)
                    auth.requestMatchers("/api/auth/**").permitAll();
                    auth.requestMatchers("/api/csrf-token").permitAll();
                    auth.requestMatchers("/error").permitAll();

                    // api is authenticated (login required)
                    auth.anyRequest().authenticated();
                })

                // Add JWT filter BEFORE (UsernamePasswordAuthenticationFilter)
                //This ensures that JWT validation happens first in filter chain
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // PasswordEncoder bean (converting password into hash)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager performing authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        // Returns the default authentication manager
        return configuration.getAuthenticationManager();
    }

    // Compromised Password Checker: checking password is strong or not
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}

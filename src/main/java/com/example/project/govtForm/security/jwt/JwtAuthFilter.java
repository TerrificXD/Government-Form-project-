package com.example.project.govtForm.security.jwt;

import com.example.project.govtForm.config.JwtProperties;
import com.example.project.govtForm.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProperties jwtProperties;


    // Main filter method that executes for every HTTP request and This is where JWT validation and user authentication happens.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = jwtProperties.getJwtHeader();  // Get the JWT header
            String authHeader = request.getHeader(header);  // Read the header value from the incoming request
            String jwt = extractJwtFromHeader(authHeader);   // Extract the JWT token from header

            // If token exists AND user is not already authenticated
            if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = jwtUtils.extractUsername(jwt);  // Extract username from JWT payload

                // If username is successfully extracted from token
                if (username != null) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);  // Load user from the database

                    // Validate JWT token
                    if (jwtUtils.isTokenValid(jwt, userDetails)) {

                        // Create authentication object for Spring Security
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,  // The authenticated user
                                        null,         // No password needed (already validated via JWT)
                                        userDetails.getAuthorities() // Roles
                                );

                        // Attach request details
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        //  Store the authentication in Spring Security's context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }
        catch (Exception e) {
            // Log any exception (token invalid, expired, signature wrong)
            logger.error("Cannot set user authentication: {}", e);
        }

        // Continue to the next filter in chain
        filterChain.doFilter(request, response);
    }

    // Helper method to extract JWT token from Authorization header.
    private String extractJwtFromHeader(String authHeader) {

        // Check if header exists and starts with "Bearer "
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {

            // Return only the JWT token part and remove Bearer
            return authHeader.substring(7);
        }

        return null;
    }
}

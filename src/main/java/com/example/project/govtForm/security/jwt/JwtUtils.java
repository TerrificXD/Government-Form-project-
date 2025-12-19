package com.example.project.govtForm.security.jwt;

import com.example.project.govtForm.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;


     // using hashing alogrithum for coveting secret string to SecretKey object for signing JWT
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getJwtSecretDefault().getBytes());
    }

    // Convert secret string to SecretKey object for signing JWT
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())  // Username is stored inside the "sub" (subject) claim
                .issuedAt(new Date())  // When token is created (timestamp)
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getJwtExpirationMs()))  // Token expiration time (24 hours)
                .signWith(getSigningKey())  // Sign token with secret key (HMAC-SHA256)
                .compact();  // Convert to compact JWT string
    }

    // Extract all claims from token (payload data)
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Set the secret key to verify the token's signature
                .build()
                .parseSignedClaims(token) // Parse the token and verify its signature
                .getPayload(); // Get the payload (body) of the JWT which contains the claims
    }

    // Extract username
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Validate token (Username matches and Token is not expired)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extract username from JWT token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));  // Compare username + check expiration
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {

        // Compare expiration date with current date
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }
}

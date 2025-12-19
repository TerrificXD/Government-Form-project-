package com.example.project.govtForm.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtProperties {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.secret.default}")
    private String jwtSecretDefault;

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;
}
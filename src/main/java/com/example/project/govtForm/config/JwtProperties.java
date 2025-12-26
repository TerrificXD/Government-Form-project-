package com.example.project.govtForm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProperties {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.secret.default}")
    private String jwtSecretDefault;

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public String getJwtSecretDefault() {
        return jwtSecretDefault;
    }

    public String getJwtHeader() {
        return jwtHeader;
    }

    public long getJwtExpirationMs() {
        return jwtExpirationMs;
    }
}

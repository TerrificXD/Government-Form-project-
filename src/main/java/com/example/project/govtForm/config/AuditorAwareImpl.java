package com.example.project.govtForm.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

    // using auditing -> this method return username of current login user
    @Override
    public Optional<String> getCurrentAuditor() {

        // getting authenticated user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1. authentication is null - no security context exists
        // 2. !authentication.isAuthenticated() - user is not authenticated
        // 3. (anonymousUser) Spring Security's default anonymous user
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equalsIgnoreCase(String.valueOf(authentication.getPrincipal()))) {

           // If no user is logged in, return empty.
            return Optional.empty();
        }

        // Extract the authenticated user
        String username = authentication.getName();

        return Optional.of(username);
    }
}
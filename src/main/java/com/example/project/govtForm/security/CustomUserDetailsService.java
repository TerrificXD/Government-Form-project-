package com.example.project.govtForm.security;

import com.example.project.govtForm.security.entity.SystemAdmin;
import com.example.project.govtForm.security.repository.SystemAdminRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SystemAdminRepository systemAdminRepository;

    // It loads user details from the database based on the provided username.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Search for the admin user in the database by username
        SystemAdmin admin = systemAdminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("System admin not found: " + username));

        // Create a list of authorities for this user and assigning a single role: "ROLE_ADMIN"
        // Spring Security expects role names to be prefixed with "ROLE_"
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Build and return a UserDetails object using Spring Security's User builder
        // This object will be used by Spring Security to perform authentication and authorization
        return User.builder()
                .username(admin.getUsername())  // Set the username from the database
                .password(admin.getPassword())  //  Set the password from the database
                .authorities(authorities)       // Set user's roles
                .build();                       // Build and return the UserDetails object
    }
}

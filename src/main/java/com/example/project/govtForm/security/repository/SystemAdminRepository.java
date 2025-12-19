package com.example.project.govtForm.security.repository;

import com.example.project.govtForm.security.entity.SystemAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemAdminRepository extends JpaRepository<SystemAdmin,Long> {

    Optional<SystemAdmin> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}

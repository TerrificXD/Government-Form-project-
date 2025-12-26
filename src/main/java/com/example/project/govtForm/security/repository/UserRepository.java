package com.example.project.govtForm.security.repository;

import com.example.project.govtForm.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Query(
            value = """
        INSERT INTO user_roles (user_id, role)
        SELECT u.id, 'ROLE_ADMIN'
        FROM users u
        JOIN employees e ON e.id = :employeeId
        WHERE u.employee_id = e.id
          AND NOT EXISTS (
              SELECT 1 FROM user_roles ur
              WHERE ur.user_id = u.id AND ur.role = 'ROLE_ADMIN'
          )
        """,
            nativeQuery = true
    )
    void promoteEmployeeUserToAdmin(Long employeeId);
}

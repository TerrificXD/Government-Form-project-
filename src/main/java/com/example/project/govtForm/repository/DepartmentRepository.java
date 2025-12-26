package com.example.project.govtForm.repository;

import com.example.project.govtForm.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByNameIgnoreCase(String name);

    @Query("""
        SELECT DISTINCT d
        FROM Department d
        LEFT JOIN FETCH d.employees
    """)
    List<Department> findAllWithEmployees();

    @Query("""
        SELECT d
        FROM Department d
        LEFT JOIN FETCH d.employees
        WHERE d.id = :id
    """)
    Optional<Department> findByIdWithEmployees(Long id);
}

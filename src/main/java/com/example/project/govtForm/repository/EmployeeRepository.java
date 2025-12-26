package com.example.project.govtForm.repository;

import com.example.project.govtForm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByUsername(String username);

    @Query(" SELECT e FROM Employee e LEFT JOIN FETCH e.department LEFT JOIN FETCH e.manager LEFT JOIN FETCH e.addresses WHERE e.username = :username")
    Optional<Employee> findProfileByUsername(@Param("username") String username);

    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    List<Employee> findByDepartmentId(@Param("departmentId") Long departmentId);
}

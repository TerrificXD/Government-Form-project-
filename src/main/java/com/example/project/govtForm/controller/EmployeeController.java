package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.EmployeeDto;
import com.example.project.govtForm.dto.EmployeeFilterRequest;
import com.example.project.govtForm.service.IEmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final IEmployeeService employeeService;

    // Get all employees
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        List<EmployeeDto> employees = employeeService.getAllEmployees();  // Fetch list of employees from service layer
        return ResponseEntity.ok(employees);
    }

    // SEARCH employees
    @PostMapping("/search")
    public ResponseEntity<Page<EmployeeDto>> searchEmployees(@RequestBody EmployeeFilterRequest filter) { // Contains search criteria
        Page<EmployeeDto> result = employeeService.searchEmployees(filter);  // Call service to run specification-based search
        return ResponseEntity.ok(result);
    }

    // Create new employee
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) { // Validates request body & maps JSON → EmployeeDto
        EmployeeDto employee = employeeService.createEmployee(employeeDto);  // Call service to create employee
        return ResponseEntity.ok(employee);
    }

    // Get employee by ID
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) { // Extracts numeric id from URL path
        EmployeeDto employee = employeeService.getEmployeeById(id);  // Fetch employee data by ID
        return ResponseEntity.ok(employee);
    }

    // Update employee by ID
    @PutMapping("/{id:\\d+}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto) { // Employee ID from URL and Updated data from request body
        EmployeeDto updateEmployee = employeeService.updateEmployee(id, employeeDto);  // Call service to update employee
        return ResponseEntity.ok(updateEmployee);
    }

    // Delete employee by ID
    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable Long id) { // Extract ID from URL
        employeeService.deleteEmployee(id);  // Delete the employee through service layer
        return ResponseEntity.noContent().build();
    }
}

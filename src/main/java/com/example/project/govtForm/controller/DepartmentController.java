package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.DepartmentDto;
import com.example.project.govtForm.service.IDepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController {

    private final IDepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        List<DepartmentDto> departments = departmentService.getAllDepartment();   // Call service to get list of departments
        return ResponseEntity.ok(departments); // Return 200 OK with list of departments
    }


    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) { // Validate and map JSON request into DTO
        DepartmentDto department = departmentService.createDepartment(departmentDto);    // Call service to create and save department
        return ResponseEntity.ok(department);  // Return 200 OK with created department
    }


    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) { // Extract dynamic value from URL
        DepartmentDto department = departmentService.getDepartmentById(id);   // Call service to find department
        return ResponseEntity.ok(department);  // Return 200 OK with department
    }


    @PutMapping("/{id}")
    // Extract department ID from URL, Validate and map request body
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentDto departmentDto) {
        DepartmentDto updateDepartment = departmentService.updateDepartment(id, departmentDto); // Call service to update department
        return ResponseEntity.ok(updateDepartment);  // Return updated department
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DepartmentDto> deleteDepartment(@PathVariable Long id) { // Get ID from URL
        departmentService.deleteDepartmentById(id);  // Call service to delete department
        return ResponseEntity.noContent().build();   // Return 204 No Content (successful but no body)
    }

}

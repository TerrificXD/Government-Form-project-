package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.DepartmentDto;
import com.example.project.govtForm.service.IDepartmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    private final IDepartmentService departmentService;

    public DepartmentController(IDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        logger.info("Request received to fetch all departments");
        List<DepartmentDto> departments = departmentService.getAllDepartments();
        logger.info("Successfully fetched {} departments", (departments != null ? departments.size() : 0));
        return ResponseEntity.ok(departments);
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        logger.info("Request received to create a new department: {}", departmentDto.getName());
        DepartmentDto department = departmentService.createDepartment(departmentDto);
        logger.info("Department created successfully with id: {}", department.getName());
        return ResponseEntity.ok(department);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) {
        logger.info("Request received to fetch department with id: {}", id);
        DepartmentDto department = departmentService.findByIdWithEmployees(id);
        logger.info("Successfully fetched department with id: {}", id);
        return ResponseEntity.ok(department);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentDto departmentDto) {
        logger.info("Request received to update department with id: {}", id);
        DepartmentDto updateDepartment = departmentService.updateDepartment(id, departmentDto);
        logger.info("Department updated successfully with id: {}", id);
        return ResponseEntity.ok(updateDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        logger.info("Request received to delete department with id: {}", id);
        departmentService.deleteDepartmentById(id);
        logger.info("Department deleted successfully with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}

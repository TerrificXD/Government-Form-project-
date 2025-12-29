package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.EmployeeAdminDto;
import com.example.project.govtForm.dto.EmployeeFilterRequest;
import com.example.project.govtForm.service.IAuthService;
import com.example.project.govtForm.service.IEmployeeAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/employees")
public class EmployeeAdminController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeAdminController.class);
    private final IEmployeeAdminService iEmployeeAdminService;
    private final IAuthService iAuthService;

    public EmployeeAdminController(IEmployeeAdminService iEmployeeAdminService, IAuthService iAuthService) {
        this.iEmployeeAdminService = iEmployeeAdminService;
        this.iAuthService = iAuthService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeAdminDto>> getAllEmployees() {
        logger.info("Admin request received to fetch all employees");
        List<EmployeeAdminDto> employees = iEmployeeAdminService.getAllEmployees();
        logger.info("Successfully fetched {} employees", (employees != null ? employees.size() : 0));
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<EmployeeAdminDto>> searchEmployees(@RequestBody EmployeeFilterRequest filter) {
        logger.info("Admin request received to search employees");
        logger.debug("Search filter received: page={}, size={}, sortBy={}, sortDir={}", filter.getPage(), filter.getSize(), filter.getSortBy(), filter.getSortDir());
        Page<EmployeeAdminDto> results = iEmployeeAdminService.searchEmployees(filter);
        logger.info("Employee search completed. Total results: {}", results != null ? results.getTotalElements() : 0);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<EmployeeAdminDto> getEmployee(@PathVariable Long id) {
        logger.info("Admin request received to fetch employee with id: {}", id);
        EmployeeAdminDto employee = iEmployeeAdminService.getEmployeeById(id);
        logger.info("Employee details retrieved successfully for id: {}", id);
        return ResponseEntity.ok(employee);

    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.warn("Admin request received to delete employee with id: {}", id);
        iEmployeeAdminService.deleteEmployee(id);
        logger.info("Employee deleted successfully with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id:\\d+}/promote-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> promoteEmployeeToAdmin(@PathVariable Long id) {
        logger.info("Admin request received to promote employee with id: {} to ADMIN", id);
        iAuthService.promoteEmployeeToAdmin(id);
        logger.info("Employee promoted to ADMIN successfully. Employee id: {}", id);
        return ResponseEntity.ok(
                Map.of(
                        "message", "Employee promoted to ADMIN successfully",
                        "employeeId", id
                )
        );
    }
}

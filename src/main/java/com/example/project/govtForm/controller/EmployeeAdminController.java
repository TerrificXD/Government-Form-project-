package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.EmployeeAdminDto;
import com.example.project.govtForm.dto.EmployeeFilterRequest;
import com.example.project.govtForm.service.IAuthService;
import com.example.project.govtForm.service.IEmployeeAdminService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/employees")
public class EmployeeAdminController {

    private final IEmployeeAdminService iEmployeeAdminService;
    private final IAuthService iAuthService;

    public EmployeeAdminController(IEmployeeAdminService iEmployeeAdminService, IAuthService iAuthService) {
        this.iEmployeeAdminService = iEmployeeAdminService;
        this.iAuthService = iAuthService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeAdminDto>> getAllEmployees() {
        return ResponseEntity.ok(iEmployeeAdminService.getAllEmployees());
    }

    @PostMapping("/search")
    public ResponseEntity<Page<EmployeeAdminDto>> searchEmployees(
            @RequestBody EmployeeFilterRequest filter) {
        return ResponseEntity.ok(iEmployeeAdminService.searchEmployees(filter));
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<EmployeeAdminDto> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(iEmployeeAdminService.getEmployeeById(id));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        iEmployeeAdminService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id:\\d+}/promote-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> promoteEmployeeToAdmin(@PathVariable Long id) {

        iAuthService.promoteEmployeeToAdmin(id);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Employee promoted to ADMIN successfully",
                        "employeeId", id
                )
        );
    }
}

package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.EmployeeDto;
import com.example.project.govtForm.service.IEmpProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile")
public class EmployeeProfileController {

    private final IEmpProfileService empProfileService;

    public EmployeeProfileController(IEmpProfileService empProfileService) {
        this.empProfileService = empProfileService;
    }

    @GetMapping
    public ResponseEntity<EmployeeDto> getMyProfile() {
        return ResponseEntity.ok(empProfileService.employeeProfile());
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createMyProfile(@Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(empProfileService.createMyProfile(dto));
    }

    @PutMapping
    public ResponseEntity<EmployeeDto> updateMyProfile(@Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(empProfileService.updateMyProfile(dto));
    }
}

package com.example.project.govtForm.controller;

import com.example.project.govtForm.dto.EmployeeDto;
import com.example.project.govtForm.service.IEmpProfileService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile")
public class EmployeeProfileController {

    public static final Logger logger = LoggerFactory.getLogger(EmployeeProfileController.class);
    private final IEmpProfileService empProfileService;

    public EmployeeProfileController(IEmpProfileService empProfileService) {
        this.empProfileService = empProfileService;
    }

    @GetMapping
    public ResponseEntity<EmployeeDto> getMyProfile() {
        logger.info("Request received to fetch logged-in employee profile");
        EmployeeDto profile = empProfileService.employeeProfile();
        logger.info("Successfully fetched logged-in employee profile");
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createMyProfile(@Valid @RequestBody EmployeeDto dto) {
        logger.info("Request received to create profile for logged-in employee");
        logger.debug("Profile create request payload received");
        EmployeeDto empCreated = empProfileService.createMyProfile(dto);
        logger.info("Profile created successfully for logged-in employee");
        return ResponseEntity.ok(empCreated);
    }

    @PutMapping
    public ResponseEntity<EmployeeDto> updateMyProfile(@Valid @RequestBody EmployeeDto dto) {
        logger.info("Request received to update logged-in employee profile");
        logger.debug("Profile update request payload received");
        EmployeeDto updateEmpProfile = empProfileService.updateMyProfile(dto);
        logger.info("Profile updated successfully for logged-in employee");
        return ResponseEntity.ok(updateEmpProfile);
    }
}

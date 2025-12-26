package com.example.project.govtForm.service;

import com.example.project.govtForm.dto.EmployeeAdminDto;
import com.example.project.govtForm.dto.EmployeeFilterRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEmployeeAdminService {

    List<EmployeeAdminDto> getAllEmployees();

    EmployeeAdminDto getEmployeeById(Long id);

    Page<EmployeeAdminDto> searchEmployees(EmployeeFilterRequest filter);

    void deleteEmployee(Long id);
}

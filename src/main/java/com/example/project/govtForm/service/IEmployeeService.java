package com.example.project.govtForm.service;

import com.example.project.govtForm.dto.EmployeeDto;
import com.example.project.govtForm.dto.EmployeeFilterRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IEmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);
    void deleteEmployee(Long id);
    EmployeeDto getEmployeeById(Long id);
    List<EmployeeDto> getAllEmployees();
    Page<EmployeeDto> searchEmployees(EmployeeFilterRequest filter);
}

//    List<EmployeeDto> getEmployeesByDepartment(Long departmentId);

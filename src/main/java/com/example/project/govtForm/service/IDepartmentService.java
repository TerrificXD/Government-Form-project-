package com.example.project.govtForm.service;

import com.example.project.govtForm.dto.DepartmentDto;
import com.example.project.govtForm.dto.EmployeeDto;

import java.util.List;

public interface IDepartmentService {
    List<DepartmentDto> getAllDepartment();
    DepartmentDto createDepartment(DepartmentDto departmentDto);
    DepartmentDto getDepartmentById(Long id);
    DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto);
    void deleteDepartmentById(Long id);

}

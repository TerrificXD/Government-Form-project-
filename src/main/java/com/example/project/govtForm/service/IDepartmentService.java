package com.example.project.govtForm.service;

import com.example.project.govtForm.dto.DepartmentDto;

import java.util.List;

public interface IDepartmentService {
    List<DepartmentDto> getAllDepartments();
    DepartmentDto createDepartment(DepartmentDto departmentDto);
    DepartmentDto findByIdWithEmployees(Long id);
    DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto);
    void deleteDepartmentById(Long id);

}

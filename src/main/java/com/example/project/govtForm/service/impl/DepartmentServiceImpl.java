package com.example.project.govtForm.service.impl;

import com.example.project.govtForm.dto.AddressDto;
import com.example.project.govtForm.dto.DepartmentDto;
import com.example.project.govtForm.dto.EmployeeDto;
import com.example.project.govtForm.dto.EmployeeSummaryDto;
import com.example.project.govtForm.entity.Address;
import com.example.project.govtForm.entity.Department;
import com.example.project.govtForm.entity.Employee;
import com.example.project.govtForm.exception.ResourceNotFoundException;
import com.example.project.govtForm.repository.DepartmentRepository;
import com.example.project.govtForm.service.IDepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements IDepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAllWithEmployees();

        return departments.stream()
                .map(department -> convertDepartmentToDto(department, true))
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {

        return departmentRepository.findByNameIgnoreCase(departmentDto.getName())
                .map(existing -> convertDepartmentToDto(existing, false))
                .orElseGet(() -> {
                    Department department = convertDtoToDepartment(departmentDto);

                    if (department.getEmployees() == null) {
                        department.setEmployees(new ArrayList<>());
                    }

                    Department saved = departmentRepository.save(department);
                    return convertDepartmentToDto(saved, false);
                });
    }

    @Override
    public DepartmentDto findByIdWithEmployees(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with id: " + id));

        return convertDepartmentToDto(department, true);
    }

    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with id: " + id));

        String newName = departmentDto.getName();

        departmentRepository.findByNameIgnoreCase(newName)
                .ifPresent(dept -> {
                    if (!dept.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Department with name '" + newName + "' already exists"
                        );
                    }
                });

        existingDepartment.setName(newName);
        existingDepartment.setDescription(departmentDto.getDescription());

        Department updated = departmentRepository.save(existingDepartment);
        return convertDepartmentToDto(updated, false);
    }

    @Override
    public void deleteDepartmentById(Long id) {
        Department existingDept = departmentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Department not found with id: " + id));

        departmentRepository.delete(existingDept);
    }


    private DepartmentDto convertDepartmentToDto(Department department, boolean includeEmployees) {
        DepartmentDto dto = new DepartmentDto();
        BeanUtils.copyProperties(department, dto);

        if (includeEmployees && department.getEmployees() != null) {

            List<EmployeeSummaryDto> employeeSummaryList =
                    department.getEmployees().stream()
                            .map(this::convertEmployeeToSummaryDto)
                            .collect(Collectors.toList());

            dto.setEmployees(employeeSummaryList);
        }

        return dto;
    }

    private EmployeeSummaryDto convertEmployeeToSummaryDto(Employee employee) {
        return new EmployeeSummaryDto(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPosition()
        );
    }

    private Department convertDtoToDepartment(DepartmentDto dto) {
        Department department = new Department();
        BeanUtils.copyProperties(dto, department);
        return department;
    }

    private EmployeeDto convertEmployeeToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName(employee.getFirstName());
        dto.setPosition(employee.getPosition());
        return dto;
    }

    private AddressDto convertAddressToDto(Address address) {
        AddressDto dto = new AddressDto();
        BeanUtils.copyProperties(address, dto);
        return dto;
    }
}

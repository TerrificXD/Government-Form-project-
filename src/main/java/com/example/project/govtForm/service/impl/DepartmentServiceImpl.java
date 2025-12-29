package com.example.project.govtForm.service.impl;

import com.example.project.govtForm.dto.DepartmentDto;
import com.example.project.govtForm.dto.EmployeeSummaryDto;
import com.example.project.govtForm.entity.Department;
import com.example.project.govtForm.entity.Employee;
import com.example.project.govtForm.exception.ResourceNotFoundException;
import com.example.project.govtForm.repository.DepartmentRepository;
import com.example.project.govtForm.service.IDepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements IDepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    //GET ALL
    @Override
    public List<DepartmentDto> getAllDepartments() {

        logger.info("Fetching all departments with employees");

        List<Department> departments = departmentRepository.findAllWithEmployees();

        if (departments == null) {
            logger.warn("Repository returned null department list");
            return Collections.emptyList();
        }

        logger.info("Successfully fetched {} departments", departments.size());

        return departments.stream()
                .map(d -> convertDepartmentToDto(d, true))
                .collect(Collectors.toList());
    }


    //CREATE
    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {

        logger.info("Creating department with name: {}", departmentDto.getName());

        departmentRepository.findByNameIgnoreCase(departmentDto.getName())
                .ifPresent(existing -> {
                    logger.warn("Department already exists with name: {}", existing.getName());
                    throw new IllegalArgumentException(
                            "Department already exists with name: " + departmentDto.getName()
                    );
                });

        Department department = convertDtoToDepartment(departmentDto);

        if (department.getEmployees() == null) {
            department.setEmployees(new ArrayList<>());
        }

        Department saved = departmentRepository.save(department);

        logger.info("Department created successfully with id: {}", saved.getId());

        return convertDepartmentToDto(saved, false);
    }


    // GET BY ID
    @Override
    public DepartmentDto findByIdWithEmployees(Long id) {

        logger.info("Fetching department with id: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Department not found with id: {}", id);
                    return new ResourceNotFoundException("Department not found with id: " + id);
                });

        logger.info("Department found with id: {}", id);

        return convertDepartmentToDto(department, true);
    }


    // UPDATE
    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {

        logger.info("Updating department with id: {}", id);

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Department not found for update with id: {}", id);
                    return new ResourceNotFoundException("Department not found with id: " + id);
                });

        String newName = departmentDto.getName();

        departmentRepository.findByNameIgnoreCase(newName)
                .ifPresent(dept -> {
                    if (!dept.getId().equals(id)) {
                        logger.warn("Department name '{}' already exists", newName);
                        throw new IllegalArgumentException(
                                "Department with name '" + newName + "' already exists"
                        );
                    }
                });

        existingDepartment.setName(newName);
        existingDepartment.setDescription(departmentDto.getDescription());

        Department updated = departmentRepository.save(existingDepartment);

        logger.info("Department updated successfully with id: {}", id);

        return convertDepartmentToDto(updated, false);
    }


    //DELETE
    @Override
    public void deleteDepartmentById(Long id) {

        logger.warn("Deleting department with id: {}", id);

        Department existingDept = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Attempted to delete non-existent department with id: {}", id);
                    return new ResourceNotFoundException("Department not found with id: " + id);
                });

        departmentRepository.delete(existingDept);

        logger.info("Department deleted successfully with id: {}", id);
    }


    // MAPPERS
    private DepartmentDto convertDepartmentToDto(Department department, boolean includeEmployees) {

        DepartmentDto dto = new DepartmentDto();
        BeanUtils.copyProperties(department, dto);

        if (includeEmployees && department.getEmployees() != null) {

            List<EmployeeSummaryDto> employeeSummaryList =
                    department.getEmployees()
                            .stream()
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
}

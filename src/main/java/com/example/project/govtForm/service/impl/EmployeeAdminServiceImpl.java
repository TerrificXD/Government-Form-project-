package com.example.project.govtForm.service.impl;

import com.example.project.govtForm.dto.AddressDto;
import com.example.project.govtForm.dto.EmployeeAdminDto;
import com.example.project.govtForm.dto.EmployeeFilterRequest;
import com.example.project.govtForm.dto.ManagerDto;
import com.example.project.govtForm.entity.Address;
import com.example.project.govtForm.entity.Employee;
import com.example.project.govtForm.exception.ResourceNotFoundException;
import com.example.project.govtForm.repository.EmployeeRepository;
import com.example.project.govtForm.service.IEmployeeAdminService;
import com.example.project.govtForm.specification.EmployeeSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeAdminServiceImpl implements IEmployeeAdminService {

    private final EmployeeRepository employeeRepository;

    // ---- Constructor Injection (No Lombok) ----
    public EmployeeAdminServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    // DELETE EMPLOYEE
    @Override
    public void deleteEmployee(Long id) {

        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(existing);
    }


    // GET EMPLOYEE BY ID
    @Override
    public EmployeeAdminDto getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found with id: " + id));

        return mapToAdminDto(employee);
    }


    // GET ALL EMPLOYEES
    @Override
    public List<EmployeeAdminDto> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToAdminDto)
                .collect(Collectors.toList());
    }


    @Override
    public Page<EmployeeAdminDto> searchEmployees(EmployeeFilterRequest filter) {

        Sort sort = filter.getSortDir().equalsIgnoreCase("DESC")
                ? Sort.by(filter.getSortBy()).descending()
                : Sort.by(filter.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                sort
        );

        Specification<Employee> spec = (root, query, cb) -> cb.conjunction();

        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and(
                    EmployeeSpecification.findFirstName(filter.getName())
                            .or(EmployeeSpecification.findLastName(filter.getName()))
            );
        }

        if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
            spec = spec.and(
                    (root, query, cb) ->
                            cb.like(
                                    cb.lower(root.get("email")),
                                    "%" + filter.getEmail().toLowerCase() + "%"
                            )
            );
        }

        if (filter.getDepartmentId() != null) {
            spec = spec.and(EmployeeSpecification.findDepartmentId(filter.getDepartmentId()));
        }

        if (filter.getPosition() != null) {
            spec = spec.and(EmployeeSpecification.findPosition(filter.getPosition()));
        }

        if (filter.getJoinDateFrom() != null) {
            spec = spec.and(EmployeeSpecification.joinDateGreaterThanEqual(filter.getJoinDateFrom()));
        }

        // --- Sorting Helpers ---

        if (Boolean.TRUE.equals(filter.getRecentlyJoined())) {
            spec = spec.and(EmployeeSpecification.recentlyJoined(filter.getDepartmentId()));
        }

        if (Boolean.TRUE.equals(filter.getLongestServing())) {
            spec = spec.and(EmployeeSpecification.longestServing(filter.getDepartmentId()));
        }

        Page<Employee> page = employeeRepository.findAll(spec, pageable);

        return page.map(this::mapToAdminDto);
    }


    // -------- Mapping Helpers --------

    private EmployeeAdminDto mapToAdminDto(Employee employee) {

        EmployeeAdminDto dto = new EmployeeAdminDto();

        BeanUtils.copyProperties(
                employee,
                dto,
                "department",
                "manager",
                "addresses"
        );

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
        }

        if (employee.getManager() != null) {
            dto.setManager(
                    new ManagerDto(
                            employee.getManager().getFirstName(),
                            employee.getManager().getLastName(),
                            employee.getManager().getEmail()
                    )
            );
        }

        if (employee.getAddresses() != null && !employee.getAddresses().isEmpty()) {
            dto.setAddresses(
                    employee.getAddresses().stream()
                            .map(this::mapToAddressDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }


    private AddressDto mapToAddressDto(Address address) {
        AddressDto dto = new AddressDto();
        BeanUtils.copyProperties(address, dto);
        return dto;
    }
}

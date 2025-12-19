package com.example.project.govtForm.service.impl;

import com.example.project.govtForm.dto.AddressDto;
import com.example.project.govtForm.dto.DepartmentDto;
import com.example.project.govtForm.dto.EmployeeDto;
import com.example.project.govtForm.entity.Address;
import com.example.project.govtForm.entity.Department;
import com.example.project.govtForm.entity.Employee;
import com.example.project.govtForm.exception.ResourceNotFoundException;
import com.example.project.govtForm.repository.DepartmentRepository;
import com.example.project.govtForm.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService {

    private final DepartmentRepository departmentRepository;

    // Get all departments from database
    @Override
    public List<DepartmentDto> getAllDepartment() {
        return departmentRepository.findAll()

                // Convert List to Stream for functional operations
                .stream()

                // Map each Department entity to DepartmentDto and (false) means don't map the employees list here.
                .map(department -> convertDepartmentToDto(department, false))

                // Collect the stream back into a List
                .toList();
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {

        // Check if a department with the same name already exists
        return departmentRepository.findByNameIgnoreCase(departmentDto.getName())

                // If department exists, convert it to DTO and return it
                .map(existing -> convertDepartmentToDto(existing, false))

                // If not found, create a new Department using the DTO and Convert DepartmentDto to Department entity.
                .orElseGet(() -> {Department department = convertDtoToDepartment(departmentDto);

                    // If employees list is null, initialize it to avoid NullPointerException later.
                    if (department.getEmployees() == null) { department.setEmployees(new ArrayList<>()); }

                    // Save the new department to database
                    Department saved = departmentRepository.save(department);

                    // Convert entity to DepartmentDto and return
                    return convertDepartmentToDto(saved, false);
                });
    }


    // Retrieves the department by its ID.
    @Override
    public DepartmentDto getDepartmentById(Long id) {
        // Search for department by ID
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        // Convert Department to DTO and include employees list in the response.
        return convertDepartmentToDto(department, true);
    }

    // Updates an existing department
    @Override
    public DepartmentDto updateDepartment(Long id, DepartmentDto departmentDto) {

        // Find the existing department
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        // Extract the new name from DTO.
        String newName = departmentDto.getName();

        // Check if any department already exists with the new name
        departmentRepository.findByNameIgnoreCase(newName)

                // If department with that name exists, and it's NOT the current department being updated.
                .ifPresent(dept -> {
                    if (!dept.getId().equals(id)) {
                        throw new IllegalArgumentException("Department with name '" + newName + "' already exists"); // Throw an exception
                    }
                });

        // Update the department's fields
        existingDepartment.setName(newName);
        existingDepartment.setDescription(departmentDto.getDescription());

        // Save the updated department to database
        Department updated = departmentRepository.save(existingDepartment);

        // Convert updated entity to DTO and return without employees.
        return convertDepartmentToDto(updated, false);
    }

    // Deletes a department by its ID.
    @Override
    public void deleteDepartmentById(Long id) {

        // Verify the department exists
        Department existingDept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        departmentRepository.delete(existingDept);   // Delete the department from database
    }


    // MAPPING HELPER METHODS
    // These methods convert between Entity and DTO objects

    // Converts a Department entity to a DepartmentDto.
    private DepartmentDto convertDepartmentToDto(Department department, boolean includeEmployees) {
        DepartmentDto dto = new DepartmentDto();
        BeanUtils.copyProperties(department, dto);   // BeanUtils copies matching properties from source to destination

        // Only map employees if `includeEmployees` AND department has employees
        if (includeEmployees && department.getEmployees() != null) {

            // Convert each Employee entity to EmployeeDto
            List<EmployeeDto> employeeDtoList = department.getEmployees().stream()

                    // Convert each employee
                    .map(this::convertEmployeeToDto)

                    // Collect into a List
                    .collect(Collectors.toList());

            // Set the list of EmployeeDto in DepartmentDto.
            dto.setEmployees(employeeDtoList);
        }

        return dto;
        // ^ Return the final DepartmentDto.
    }

    // Converts a DepartmentDto to Department entity.
    private Department convertDtoToDepartment(DepartmentDto dto) {
        Department department = new Department();
        BeanUtils.copyProperties(dto, department); // Copy properties
        return department; // Return Department entity.
    }

    // Converts an Employee entity to EmployeeDto.
    private EmployeeDto convertEmployeeToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        BeanUtils.copyProperties(employee, dto); // Copy properties

        // Set departmentId in EmployeeDto and if employee has a department, set its ID. Otherwise, set null.
        dto.setDepartmentId(employee.getDepartment() != null ? employee.getDepartment().getId() : null);

        // Convert employee's addresses to AddressDto list
        if (employee.getAddresses() != null) {
            List<AddressDto> addressDtoList = employee.getAddresses().stream()
                    .map(this::convertAddressToDto)   // Convert each Address entity to AddressDto.
                    .collect(Collectors.toList()); // Collect them into List
            dto.setAddresses(addressDtoList);  // Set the list of AddressDto into EmployeeDto.
        }

        return dto; // EmployeeDto.
    }

    //  Converts an Address entity to AddressDto.
    private AddressDto convertAddressToDto(Address address) {
        AddressDto dto = new AddressDto();
        BeanUtils.copyProperties(address, dto);  // Copy properties
        return dto; // AddressDto.
    }
}

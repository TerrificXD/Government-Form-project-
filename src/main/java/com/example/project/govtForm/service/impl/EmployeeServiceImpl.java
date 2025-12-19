package com.example.project.govtForm.service.impl;

import com.example.project.govtForm.dto.AddressDto;
import com.example.project.govtForm.dto.EmployeeDto;
import com.example.project.govtForm.dto.EmployeeFilterRequest;
import com.example.project.govtForm.entity.Address;
import com.example.project.govtForm.entity.Department;
import com.example.project.govtForm.entity.Employee;
import com.example.project.govtForm.entity.enums.AddressType;
import com.example.project.govtForm.exception.ResourceNotFoundException;
import com.example.project.govtForm.repository.AddressRepository;
import com.example.project.govtForm.repository.DepartmentRepository;
import com.example.project.govtForm.repository.EmployeeRepository;
import com.example.project.govtForm.service.IEmployeeService;
import com.example.project.govtForm.specification.EmployeeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    // private final AddressRepository addressRepository;

    // CREATE EMPLOYEE
    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        // Find department by ID from the DTO.
        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeDto.getDepartmentId()));

        Employee employee = mapToEntity(employeeDto); // Convert EmployeeDto to Employee entity object
        employee.setDepartment(department); // Set the department in the employee entity
        List<Address> addresses = new ArrayList<>(); // add address list for this employee

        // Check if employee has addresses in the EmployeeDto
        if (employeeDto.getAddresses() != null) {

            // Loop through each AddressDto and convert to Address entity
            for (AddressDto addressDto : employeeDto.getAddresses()) {
                Address address = mapToAddressEntity(addressDto);
                address.setEmployee(employee);   // Set back reference: address belongs to this employee
                addresses.add(address);  // Add to the list
            }
        }
        employee.setAddresses(addresses);  // Set the addresses list into the employee entity

        // Save employee to database and this will also save all addresses automatically (Because of CascadeType.ALL)
        Employee saved = employeeRepository.save(employee);

        return mapToDto(saved);  // employeeDto
    }

    // UPDATE EMPLOYEE
    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        // Find existing employee by ID
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Update fields from DTO to entity
        existing.setFirstName(employeeDto.getFirstName());
        existing.setLastName(employeeDto.getLastName());
        existing.setEmail(employeeDto.getEmail());
        existing.setPhone(employeeDto.getPhone());
        existing.setPosition(employeeDto.getPosition());
        existing.setJoinDate(employeeDto.getJoinDate());

        // If departmentId provided in DTO, update department as well
        if (employeeDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeDto.getDepartmentId()));
            existing.setDepartment(department);
        }

        existing.getAddresses().clear();  // Update addresses (clear old addresses and add new ones again)

        // Add new addresses from DTO
        if (employeeDto.getAddresses() != null) {
            for (AddressDto addressDto : employeeDto.getAddresses()) {
                Address address = mapToAddressEntity(addressDto);  // Convert DTO to entity
                address.setEmployee(existing);   // Set parent employee
                existing.getAddresses().add(address);  // Add to employee's address list
            }
        }
        Employee updated = employeeRepository.save(existing);   // Save updated employee in DB

        return mapToDto(updated);  // Convert entity to DTO
    }

    // DELETE EMPLOYEE
    @Override
    public void deleteEmployee(Long id) {

        // Check if employee exists
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employeeRepository.delete(existing);   // Delete the employee from database
    }

    // GET EMPLOYEE BY ID
    @Override
    public EmployeeDto getEmployeeById(Long id) {

        // Find employee by ID
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToDto(employee); // Convert entity to DTO
    }

    // GET ALL EMPLOYEES
    @Override
    public List<EmployeeDto> getAllEmployees() {
        // Find all employees from DB
        return employeeRepository.findAll().stream()

                // Convert Employee entity to EmployeeDto
                .map(this::mapToDto)

                // Collect into a List
                .collect(Collectors.toList());
    }

    // SEARCH EMPLOYEES WITH FILTERS + PAGINATION and SORTING
    @Override
    public Page<EmployeeDto> searchEmployees(EmployeeFilterRequest filter) {

        // Build Sort object based on filter.sortBy and filter.sortDir
        Sort sort = filter.getSortDir().equalsIgnoreCase("DESC")
                ? Sort.by(filter.getSortBy()).descending()
                : Sort.by(filter.getSortBy()).ascending();

        // Build Pageable object for pagination and sorting
        Pageable pageable = PageRequest.of(
                filter.getPage(),  // page number (0-based index)
                filter.getSize(),  // page size (how many records per page)
                sort               // sorting information
        );

        // Start with a specification
        Specification<Employee> spec = (root, query, cb) -> cb.conjunction();

        // Filter - name (search in firstName or lastName)
        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and(
                    // This will search for the name in either firstName or lastName fields
                     EmployeeSpecification.findFirstName(filter.getName())
                            .or(EmployeeSpecification.findLastName(filter.getName()))
            );
        }

        // Filter - email
        if (filter.getEmail() != null && !filter.getEmail().isBlank()) {
            String email = filter.getEmail().toLowerCase();
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("email")), "%" + email + "%")
            );
        }

        // Filter - departmentId
        if (filter.getDepartmentId() != null) {
            spec = spec.and(EmployeeSpecification.findDepartmentId(filter.getDepartmentId()));
        }

        // Filter - position
        if (filter.getPosition() != null) {
            spec = spec.and(EmployeeSpecification.findPosition(filter.getPosition()));
        }

        // Filter - joinDateFrom
        if (filter.getJoinDateFrom() != null) {
            spec = spec.and(EmployeeSpecification.joinDateGreaterThanEqual(filter.getJoinDateFrom()));
        }

        // Execute DB query with all specifications + pagination
        Page<Employee> page = employeeRepository.findAll(spec, pageable);

        // Convert Page<Employee> to Page<EmployeeDto> using map()
        return page.map(this::mapToDto);
    }


    // MAPPING HELPER METHODS (Entity - DTO)

    // Convert Employee entity to EmployeeDto
    private EmployeeDto mapToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();

        // Copy properties from entity to DTO and "department" and "addresses" are ignored here. handle them manually
        BeanUtils.copyProperties(employee, dto, "department", "addresses");

        // Manually set departmentId (DTO has departmentId)
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
        }

        // Manually map list of Address entities to list of AddressDto
        if (employee.getAddresses() != null) {
            List<AddressDto> addressDtos = employee.getAddresses().stream()
                    .map(this::mapToAddressDto)  // convert each Address -> AddressDto
                    .collect(Collectors.toList());
            dto.setAddresses(addressDtos);
        }
        return dto;
    }

    // Convert EmployeeDto to Employee entity
    private Employee mapToEntity(EmployeeDto dto) {
        Employee employee = new Employee();

        // Copy properties from DTO to entity and Ignore "departmentId" and "addresses" here
        BeanUtils.copyProperties(dto, employee, "departmentId", "addresses");

        // Map AddressDto to Address entities
        if (dto.getAddresses() != null) {
            List<Address> addresses = dto.getAddresses().stream()
                    .map(this::mapToAddressEntity)  // convert each AddressDto -> Address
                    .collect(Collectors.toList());
            employee.setAddresses(addresses);
        }
        return employee;
    }

    // Convert Address entity to AddressDto
    private AddressDto mapToAddressDto(Address address) {
        AddressDto dto = new AddressDto();
        BeanUtils.copyProperties(address, dto);  // Copy properties
        return dto;
    }

    // Convert AddressDto to Address entity
    private Address mapToAddressEntity(AddressDto dto) {
        Address address = new Address();
        BeanUtils.copyProperties(dto, address);  // Copy fields from DTO to entity

        // If address type is not provided (null), set default as CURRENT
        if (address.getType() == null) {
            address.setType(AddressType.CURRENT);
        }
        return address;
    }
}

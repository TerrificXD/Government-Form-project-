package com.example.project.govtForm.service.impl;

import com.example.project.govtForm.dto.AddressDto;
import com.example.project.govtForm.dto.EmployeeDto;
import com.example.project.govtForm.dto.ManagerDto;
import com.example.project.govtForm.entity.Address;
import com.example.project.govtForm.entity.Department;
import com.example.project.govtForm.entity.Employee;
import com.example.project.govtForm.entity.enums.AddressType;
import com.example.project.govtForm.exception.ResourceNotFoundException;
import com.example.project.govtForm.repository.DepartmentRepository;
import com.example.project.govtForm.repository.EmployeeRepository;
import com.example.project.govtForm.security.enums.Role;
import com.example.project.govtForm.service.IEmpProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpProfileServiceImpl implements IEmpProfileService {

    private static final Logger logger = LoggerFactory.getLogger(EmpProfileServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmpProfileServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public EmployeeDto employeeProfile() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        logger.info("Fetching profile for logged-in user '{}'", username);

        Employee employee = employeeRepository.findProfileByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Profile not found for user '{}'", username);
                    return new ResourceNotFoundException("Employee profile not found");
                });

        logger.info("Profile fetched successfully for user '{}'", username);

        return mapToDto(employee);
    }

    // CREATE
    @Override
    public EmployeeDto createMyProfile(EmployeeDto employeeDto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        logger.info("Creating profile for logged-in user '{}'", username);

        if (employeeRepository.findByUsername(username).isPresent()) {
            logger.warn("Profile creation failed — profile already exists for user '{}'", username);
            throw new RuntimeException("Profile already exists");
        }

        Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                .orElseThrow(() -> {
                    logger.warn("Department not found with id {} during profile creation", employeeDto.getDepartmentId());
                    return new ResourceNotFoundException("Department not found with id: " + employeeDto.getDepartmentId());
                });

        Employee employee = mapToEntity(employeeDto);
        employee.setDepartment(department);
        employee.setUsername(username);       // from JWT
        employee.setRole(Role.ROLE_USER);

        List<Address> addresses = new ArrayList<>();

        if (employeeDto.getAddresses() != null) {
            for (AddressDto addressDto : employeeDto.getAddresses()) {
                Address address = mapToAddressEntity(addressDto);
                address.setEmployee(employee);
                addresses.add(address);
            }
        }

        employee.setAddresses(addresses);

        Employee saved = employeeRepository.save(employee);
        logger.info("Profile created successfully for user '{}'", username);
        return mapToDto(saved);
    }


    // UPDATE
    @Override
    public EmployeeDto updateMyProfile(EmployeeDto employeeDto) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        logger.info("Updating profile for logged-in user '{}'", username);

        Employee existing = employeeRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("Profile not found for user '{}' during update", username);
                    return new ResourceNotFoundException("Employee profile not found");
                });

        existing.setFirstName(employeeDto.getFirstName());
        existing.setLastName(employeeDto.getLastName());
        existing.setEmail(employeeDto.getEmail());
        existing.setPhone(employeeDto.getPhone());
        existing.setPosition(employeeDto.getPosition());
        existing.setJoinDate(employeeDto.getJoinDate());

        if (employeeDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                    .orElseThrow(() -> {
                        logger.warn("Department not found with id {} during profile update", employeeDto.getDepartmentId());
                        return new ResourceNotFoundException("Department not found with id: " + employeeDto.getDepartmentId());
                    });
            existing.setDepartment(department);
        }

        existing.getAddresses().clear();

        if (employeeDto.getAddresses() != null) {
            for (AddressDto addressDto : employeeDto.getAddresses()) {
                Address address = mapToAddressEntity(addressDto);
                address.setEmployee(existing);
                existing.getAddresses().add(address);
            }
        }

        Employee updated = employeeRepository.save(existing);
        logger.info("Profile updated successfully for user '{}'", username);
        return mapToDto(updated);
    }


    //MAPPING HELPERS

    private Employee mapToEntity(EmployeeDto dto) {

        Employee employee = new Employee();
        BeanUtils.copyProperties(dto, employee, "username", "manager", "addresses");
        return employee;
    }

    private EmployeeDto mapToDto(Employee employee) {

        EmployeeDto dto = new EmployeeDto();
        BeanUtils.copyProperties(employee, dto, "username", "password", "department", "manager", "addresses");

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
        }

        if (employee.getAddresses() != null) {
            List<AddressDto> addressDtos = employee.getAddresses()
                    .stream()
                    .map(this::mapToAddressDto)
                    .toList();
            dto.setAddresses(addressDtos);
        }

        if (employee.getManager() != null) {
            ManagerDto managerDto = new ManagerDto();
            managerDto.setFirstName(employee.getManager().getFirstName());
            managerDto.setLastName(employee.getManager().getLastName());
            managerDto.setEmail(employee.getManager().getEmail());
            dto.setManager(managerDto);
        }

        return dto;
    }

    private AddressDto mapToAddressDto(Address address) {
        AddressDto dto = new AddressDto();
        BeanUtils.copyProperties(address, dto);
        return dto;
    }

    private Address mapToAddressEntity(AddressDto dto) {

        Address address = new Address();
        BeanUtils.copyProperties(dto, address);

        if (address.getType() == null) {
            address.setType(AddressType.CURRENT);
        }
        return address;
    }
}

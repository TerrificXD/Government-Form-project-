package com.example.project.govtForm.dto;

import com.example.project.govtForm.entity.enums.EmployeePosition;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class EmployeeDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(max = 20, message = "Phone cannot be longer than 20 characters")
    private String phone;

    @NotNull(message = "Employee position is required")
    private EmployeePosition position;

    @NotNull(message = "Join date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    @NotNull(message = "Department id is required")
    private Long departmentId;

    private String username;

    private Long managerId;

    private ManagerDto manager;

    @Valid
    private List<@NotNull(message = "Address cannot be null") AddressDto> addresses;


    // ==== Constructors ====

    public EmployeeDto() {
    }

    public EmployeeDto(String firstName, String lastName, String email,
                       String phone, EmployeePosition position,
                       LocalDate joinDate, Long departmentId,
                       String username, Long managerId,
                       ManagerDto manager,
                       List<AddressDto> addresses) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.joinDate = joinDate;
        this.departmentId = departmentId;
        this.username = username;
        this.managerId = managerId;
        this.manager = manager;
        this.addresses = addresses;
    }


    // ==== Getters & Setters ====

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public EmployeePosition getPosition() { return position; }

    public void setPosition(EmployeePosition position) { this.position = position; }

    public LocalDate getJoinDate() { return joinDate; }

    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    public Long getDepartmentId() { return departmentId; }

    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public Long getManagerId() { return managerId; }

    public void setManagerId(Long managerId) { this.managerId = managerId; }

    public ManagerDto getManager() { return manager; }

    public void setManager(ManagerDto manager) { this.manager = manager; }

    public List<AddressDto> getAddresses() { return addresses; }

    public void setAddresses(List<AddressDto> addresses) { this.addresses = addresses; }
}

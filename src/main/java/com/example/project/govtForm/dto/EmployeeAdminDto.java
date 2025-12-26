package com.example.project.govtForm.dto;

import com.example.project.govtForm.entity.enums.EmployeePosition;
import com.example.project.govtForm.security.enums.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class EmployeeAdminDto {

    private String username;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Email
    @NotBlank
    @Size(max = 255)
    private String email;

    @Size(max = 20)
    private String phone;

    @NotNull
    private EmployeePosition position;

    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    @NotNull
    private Long departmentId;

    @NotNull
    private Role role;

    private boolean active = true;

    private ManagerDto manager;

    @Valid
    private List<@NotNull AddressDto> addresses;


    public EmployeeAdminDto() {}

    // Getters & Setters
    // (Generated manually)

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

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

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public ManagerDto getManager() { return manager; }

    public void setManager(ManagerDto manager) { this.manager = manager; }

    public List<AddressDto> getAddresses() { return addresses; }

    public void setAddresses(List<AddressDto> addresses) { this.addresses = addresses; }
}

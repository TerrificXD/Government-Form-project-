package com.example.project.govtForm.dto;

import com.example.project.govtForm.entity.enums.EmployeePosition;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private EmployeePosition position;   // PERMANENT, TRAINEE, etc.

    @NotNull(message = "Join date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    @NotNull(message = "Department id is required")
    private Long departmentId;

    @Valid
    private List<@NotNull(message = "Address cannot be null") AddressDto> addresses;
}



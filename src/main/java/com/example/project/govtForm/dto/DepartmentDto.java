package com.example.project.govtForm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDto {

    @NotBlank(message = "Department name is required")
    @Size(max = 255, message = "Department name cannot be longer than 255 characters")
    private String name;

    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;        // what this department does

    private List<EmployeeDto> employees; // employees under this department

}

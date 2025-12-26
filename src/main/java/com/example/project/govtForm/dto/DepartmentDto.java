package com.example.project.govtForm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class DepartmentDto {

    @NotBlank(message = "Department name is required")
    @Size(max = 255, message = "Department name cannot be longer than 255 characters")
    private String name;

    @Size(max = 500, message = "Description cannot be longer than 500 characters")
    private String description;

    private List<EmployeeSummaryDto> employees;

    public DepartmentDto() {}

    public DepartmentDto(String name, String description, List<EmployeeSummaryDto> employees) {
        this.name = name;
        this.description = description;
        this.employees = employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EmployeeSummaryDto> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeSummaryDto> employees) {
        this.employees = employees;
    }
}

package com.example.project.govtForm.dto;

import com.example.project.govtForm.entity.enums.EmployeePosition;

public class EmployeeSummaryDto {

    private String firstName;
    private String lastName;
    private EmployeePosition position;

    public EmployeeSummaryDto() {}

    public EmployeeSummaryDto(String firstName, String lastName, EmployeePosition position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }
}

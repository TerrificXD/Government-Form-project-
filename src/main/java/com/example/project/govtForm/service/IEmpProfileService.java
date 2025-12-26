package com.example.project.govtForm.service;

import com.example.project.govtForm.dto.EmployeeDto;

public interface IEmpProfileService {

    EmployeeDto employeeProfile();
    EmployeeDto createMyProfile(EmployeeDto dto);
    EmployeeDto updateMyProfile(EmployeeDto dto);

}

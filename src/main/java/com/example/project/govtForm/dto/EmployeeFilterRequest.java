package com.example.project.govtForm.dto;

import com.example.project.govtForm.entity.enums.EmployeePosition;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeFilterRequest {

    private String name;
    private String email;
    private Long departmentId;

    private EmployeePosition position;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDateFrom;


    // PAGINATION & SORTING


    private Integer page = 0;
    private Integer size = 10;

    private String sortBy = "id";
    private String sortDir = "ASC";
}

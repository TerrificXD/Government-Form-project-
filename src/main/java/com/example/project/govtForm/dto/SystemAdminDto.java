package com.example.project.govtForm.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemAdminDto {

    private String username;
    private String email;
    private String phone;
}

package com.example.project.govtForm.dto;

import com.example.project.govtForm.entity.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    @NotBlank(message = "Address line1 is required")
    @Size(max = 255, message = "Line1 cannot be longer than 255 characters")
    private String line1;

    @Size(max = 255, message = "Line2 cannot be longer than 255 characters")
    private String line2;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pin code is required")
    private String pinCode;

    @NotBlank(message = "Country is required")
    private String country;

    @NotNull(message = "Address type is required (PERMANENT or CURRENT)")
    private AddressType type;
}

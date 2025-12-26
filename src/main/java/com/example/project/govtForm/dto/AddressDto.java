package com.example.project.govtForm.dto;

import com.example.project.govtForm.entity.enums.AddressType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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


    // ===== Constructors =====

    public AddressDto() {
    }

    public AddressDto(String line1, String line2, String city,
                      String state, String pinCode,
                      String country, AddressType type) {

        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.country = country;
        this.type = type;
    }


    // ===== Getters & Setters =====

    public String getLine1() { return line1; }

    public void setLine1(String line1) { this.line1 = line1; }

    public String getLine2() { return line2; }

    public void setLine2(String line2) { this.line2 = line2; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getPinCode() { return pinCode; }

    public void setPinCode(String pinCode) { this.pinCode = pinCode; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public AddressType getType() { return type; }

    public void setType(AddressType type) { this.type = type; }
}

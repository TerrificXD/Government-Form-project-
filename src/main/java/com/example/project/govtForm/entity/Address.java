package com.example.project.govtForm.entity;

import com.example.project.govtForm.entity.enums.AddressType;
import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String line1;
    private String line2;
    private String city;
    private String state;
    private String pinCode;
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;


    // ===== Constructors =====

    // Required by JPA
    public Address() {
    }

    public Address(Long id,
                   String line1,
                   String line2,
                   String city,
                   String state,
                   String pinCode,
                   String country,
                   AddressType type,
                   Employee employee) {

        this.id = id;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.country = country;
        this.type = type;
        this.employee = employee;
    }


    // ===== Getters & Setters =====

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public Employee getEmployee() { return employee; }

    public void setEmployee(Employee employee) { this.employee = employee; }
}

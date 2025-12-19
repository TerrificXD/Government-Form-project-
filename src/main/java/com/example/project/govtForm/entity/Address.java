package com.example.project.govtForm.entity;

import com.example.project.govtForm.entity.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "addresses")
public class Address  extends BaseEntity{
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
    @JoinColumn(name = "employee_id") // foreign key column in the Address table
    private Employee employee;
}

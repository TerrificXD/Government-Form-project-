package com.example.project.govtForm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "departments")
public class Department extends BaseEntity{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String name;

        @Column(length = 500)
        private String description;

        @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Employee> employees =  new ArrayList<>();

}

package com.example.project.govtForm.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department extends BaseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String name;

        @Column(length = 500)
        private String description;

        @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Employee> employees = new ArrayList<>();

        public Department() {}

        public Department(Long id, String name, String description, List<Employee> employees) {
                this.id = id;
                this.name = name;
                this.description = description;
                this.employees = employees;
        }

        public Long getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public List<Employee> getEmployees() {
                return employees;
        }

        public void setEmployees(List<Employee> employees) {
                this.employees = employees;
        }
}

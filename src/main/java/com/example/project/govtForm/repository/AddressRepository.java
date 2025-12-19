package com.example.project.govtForm.repository;

import com.example.project.govtForm.entity.Address;
import com.example.project.govtForm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    // All addresses of a particular employee
    List<Address> findByEmployee(Employee employee);
}

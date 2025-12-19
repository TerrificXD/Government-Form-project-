package com.example.project.govtForm.specification;

import com.example.project.govtForm.entity.Employee;
import com.example.project.govtForm.entity.enums.EmployeePosition;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class EmployeeSpecification {
    public static Specification<Employee> findFirstName(String name){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("firstName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Employee> findLastName(String name){
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("lastName")), "%" + name.toLowerCase() + "%");
    }


    public static Specification<Employee> findDepartmentId(Long departmentId){
        return (root, query, cb) ->
                cb.equal(root.get("department").get("id"), departmentId);
    }

    public static Specification<Employee> findPosition(EmployeePosition position){
        return (root, query, cb) ->
                cb.equal(root.get("position"), position);
    }

    public static Specification<Employee> joinDateGreaterThanEqual(LocalDate from){
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("joinDate"), from);
    }

    // findTop5ByDepartmentOrderByJoinDateDesc
    public static Specification<Employee> recentlyJoined(Long departmentId) {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("joinDate")));  // Sort inside criteria
            return cb.equal(root.get("department").get("id"), departmentId);
        };
    }

    // findTop5ByDepartmentOrderByJoinDateAsc
    public static Specification<Employee> longestServing(Long departmentId) {
        return (root, query, cb) -> {
            query.orderBy(cb.asc(root.get("joinDate")));   // Sort inside criteria
            return cb.equal(root.get("department").get("id"), departmentId);
        };
    }
}

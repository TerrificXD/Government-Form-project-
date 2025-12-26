package com.example.project.govtForm.dto;

import com.example.project.govtForm.entity.enums.EmployeePosition;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class EmployeeFilterRequest {

    private String name;
    private String email;
    private Long departmentId;
    private EmployeePosition position;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDateFrom;

    // Sorting + Pagination
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDir = "ASC";

    private Boolean recentlyJoined;
    private Boolean longestServing;


    public EmployeeFilterRequest() {}

    // ---------- Getters & Setters ----------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }

    public LocalDate getJoinDateFrom() {
        return joinDateFrom;
    }

    public void setJoinDateFrom(LocalDate joinDateFrom) {
        this.joinDateFrom = joinDateFrom;
    }

    public Integer getPage() {
        return page != null ? page : 0;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size != null ? size : 10;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy != null ? sortBy : "id";
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDir() {
        return sortDir != null ? sortDir : "ASC";
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public Boolean getRecentlyJoined() {
        return recentlyJoined;
    }

    public void setRecentlyJoined(Boolean recentlyJoined) {
        this.recentlyJoined = recentlyJoined;
    }

    public Boolean getLongestServing() {
        return longestServing;
    }

    public void setLongestServing(Boolean longestServing) {
        this.longestServing = longestServing;
    }
}

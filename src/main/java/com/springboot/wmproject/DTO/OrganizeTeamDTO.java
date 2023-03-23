package com.springboot.wmproject.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.wmproject.entities.Employees;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;

@Data
public class OrganizeTeamDTO {
    private int id;
    private String teamName;
    private boolean is_deleted;
    private Integer teamsize;
    @JsonIgnore
    private Collection<Employees> employeesById = new HashSet<>();

    public Collection<Employees> getEmployeesById() {
        return employeesById;
    }

    public void setEmployeesById(Collection<Employees> employeesById) {
        this.employeesById = employeesById;
        this.teamsize=employeesById.size();
    }
}

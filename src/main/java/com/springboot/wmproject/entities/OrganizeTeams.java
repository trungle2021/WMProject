package com.springboot.wmproject.entities;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "organize_teams", schema = "wmproject", catalog = "")
public class OrganizeTeams {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "team_name", nullable = true, length = 45)
    private String teamName;
    @OneToMany(mappedBy = "organizeTeamsByTeamId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Employees> employeesById = new HashSet<>();
    @OneToMany(mappedBy = "organizeTeamsByOrganizeTeam",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> ordersById = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizeTeams that = (OrganizeTeams) o;
        return id == that.id && Objects.equals(teamName, that.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teamName);
    }

    public Collection<Employees> getEmployeesById() {
        return employeesById;
    }

    public void setEmployeesById(Collection<Employees> employeesById) {
        this.employeesById = employeesById;
    }

    public Collection<Orders> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<Orders> ordersById) {
        this.ordersById = ordersById;
    }
}

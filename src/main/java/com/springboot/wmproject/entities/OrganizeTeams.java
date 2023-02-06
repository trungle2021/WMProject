package com.springboot.wmproject.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonManagedReference
    @OneToMany(mappedBy = "organizeTeams",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Employees> employees;
    @JsonManagedReference
    @OneToMany(mappedBy = "organizeTeams",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> orders;


}

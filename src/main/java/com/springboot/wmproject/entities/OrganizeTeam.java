package com.springboot.wmproject.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organize_teams")
public class OrganizeTeam {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "team_name", length = 45)
    private String teamName;



}
package com.springboot.wmproject.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TeamSummary {
    @Id
    private int team_id;
    private String team_name;
    private Integer total_members;
    private String leader_name;
    private Integer emp_id;
    private Boolean has_confirm_order;

}

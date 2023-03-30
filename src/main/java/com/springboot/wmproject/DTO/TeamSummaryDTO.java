package com.springboot.wmproject.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeamSummaryDTO {
    private int team_id;
    private String team_name;
    private Integer total_members;
    private String leader_name;
    private Integer emp_id;
    private Boolean ableToDelete;

}

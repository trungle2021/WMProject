package com.springboot.wmproject.DTO;

import lombok.Data;

@Data
public class OrganizeTeamDTO {
    private int id;
    private String teamName;
    private boolean is_deleted;
}

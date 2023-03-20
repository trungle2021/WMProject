package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.OrganizeTeamDTO;
import com.springboot.wmproject.DTO.TeamSummaryDTO;
import com.springboot.wmproject.entities.TeamSummary;

import java.util.List;

public interface OrganizeTeamService {
    List<OrganizeTeamDTO> getAllOrganizeTeam();
    List<OrganizeTeamDTO> searchOrganizeTeamByName(String name);
    List<OrganizeTeamDTO> getOrganizeTeamByName(String name);
    OrganizeTeamDTO getOneOrganizeTeamById(Integer id);
    OrganizeTeamDTO createOrganizeTeam(OrganizeTeamDTO newOrganizeTeam);
    OrganizeTeamDTO updateOrganizeTeam(OrganizeTeamDTO editOrganizeTeam);
    void softDelete(int id);

    List<TeamSummaryDTO> getSummaryTeamOrganization();

}

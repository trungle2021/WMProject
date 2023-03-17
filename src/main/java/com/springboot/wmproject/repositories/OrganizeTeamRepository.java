package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.OrganizeTeams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizeTeamRepository extends JpaRepository<OrganizeTeams,Integer> {



    @Query("Select a from OrganizeTeams a where a.teamName =:name")
    List<OrganizeTeams> getOrganizeTeamsByName(String name);
    @Query("Select a from OrganizeTeams a where a.teamName like %:name%")
    List<OrganizeTeams> searchOrganizeTeamsByName(String name);


}

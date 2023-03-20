package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.OrganizeTeamDTO;
import com.springboot.wmproject.DTO.TeamSummaryDTO;
import com.springboot.wmproject.entities.OrganizeTeams;
import com.springboot.wmproject.entities.TeamSummary;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.EmployeeRepository;
import com.springboot.wmproject.repositories.OrderRepository;
import com.springboot.wmproject.repositories.OrganizeTeamRepository;
import com.springboot.wmproject.repositories.TeamSummaryRepository;
import com.springboot.wmproject.services.OrganizeTeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.springboot.wmproject.utils.SD.teamNameRegexDenied;

@Service
public class OrganizeTeamServiceImpl implements OrganizeTeamService {
    private OrganizeTeamRepository organizeTeamRepository;
    private OrderRepository orderRepository;
    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;
    private TeamSummaryRepository teamSummaryRepository;

    @Autowired
    public OrganizeTeamServiceImpl(TeamSummaryRepository teamSummaryRepository,OrganizeTeamRepository organizeTeamRepository, OrderRepository orderRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.organizeTeamRepository = organizeTeamRepository;
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.teamSummaryRepository = teamSummaryRepository;
    }


    @Override
    public List<OrganizeTeamDTO> getAllOrganizeTeam() {
        return organizeTeamRepository.findAll().stream().filter(team -> team.isIs_deleted() == false).map(organizeTeams -> mapToDTO(organizeTeams)).collect(Collectors.toList());
    }

    @Override
    public List<OrganizeTeamDTO> searchOrganizeTeamByName(String name)throws ResourceNotFoundException {
        List<OrganizeTeamDTO> organizeTeams=organizeTeamRepository.searchOrganizeTeamsByName(name).stream().map(organizeTeam ->mapToDTO(organizeTeam)).collect(Collectors.toList());
        return organizeTeams;
    }

    @Override
    public List<OrganizeTeamDTO> getOrganizeTeamByName(String name) {
        List<OrganizeTeamDTO> organizeTeams=organizeTeamRepository.getOrganizeTeamsByName(name).stream().map(organizeTeam ->mapToDTO(organizeTeam)).collect(Collectors.toList());
        return organizeTeams;
    }

    @Override
    public OrganizeTeamDTO getOneOrganizeTeamById(Integer id) {
        OrganizeTeams organizeTeams=organizeTeamRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Organize Team","Id",String.valueOf(id)));
        return mapToDTO(organizeTeams);
    }

    @Override
    public OrganizeTeamDTO createOrganizeTeam(OrganizeTeamDTO newOrganizeTeam) throws ResourceNotFoundException{
        String teamName = newOrganizeTeam.getTeamName();


        if(teamName!=null){
            String validTeamName = teamName;
            for (String regex: teamNameRegexDenied) {
                validTeamName = validTeamName.replaceAll(regex,"");
            }
                newOrganizeTeam.setTeamName(validTeamName);
                OrganizeTeams teams= organizeTeamRepository.save(mapToEntity(newOrganizeTeam));
                String newName = teams.getTeamName() + teams.getId();
                teams.setTeamName(newName);
                OrganizeTeams teamUpdateName= organizeTeamRepository.save(teams);
                return mapToDTO(teamUpdateName);
        }
        return null;
    }

    @Override
    public OrganizeTeamDTO updateOrganizeTeam(OrganizeTeamDTO editOrganizeTeam)throws ResourceNotFoundException {

        int organizeTeamId=editOrganizeTeam.getId();

        OrganizeTeams checkOrganizeTeam=organizeTeamRepository.findById(organizeTeamId).orElseThrow(()->new ResourceNotFoundException("Organize Team","Id",String.valueOf(organizeTeamId)));
        if(checkOrganizeTeam!=null){
            String newName = editOrganizeTeam.getTeamName();
            String validTeamName = newName;
            for (String regex: teamNameRegexDenied) {
                validTeamName = validTeamName.replaceAll(regex,"");
            }
            checkOrganizeTeam.setTeamName(validTeamName  + checkOrganizeTeam.getId());
            OrganizeTeamDTO organizeTeamDTO = mapToDTO(organizeTeamRepository.save(checkOrganizeTeam));
            return organizeTeamDTO;
        }
        return null;
    }



    @Override
    public void softDelete(int id)throws ResourceNotFoundException {

        OrganizeTeams teams=organizeTeamRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Organize Team","id",String.valueOf(id)));
        teams.setIs_deleted(true);
        organizeTeamRepository.save(teams);
    }

    @Override
    public List<TeamSummaryDTO> getSummaryTeamOrganization() {
        List<TeamSummaryDTO> teamSummaries = teamSummaryRepository.getSummaryTeamOrganization().stream()
                .map(object -> {
                    TeamSummary entity = mapToTeamSummaryEntity(object);
                    if (entity.getTotal_members() == null) {
                        entity.setTotal_members(0);
                    }
                    return mapToTeamSummaryDTO(entity);
                })
                .collect(Collectors.toList());
        return teamSummaries;
    }
    public OrganizeTeams mapToEntity(OrganizeTeamDTO organizeTeamDTO) {
        OrganizeTeams organizeTeams = modelMapper.map(organizeTeamDTO, OrganizeTeams.class);
        return organizeTeams;
    }

    public OrganizeTeamDTO mapToDTO(OrganizeTeams organizeTeams) {
        OrganizeTeamDTO organizeTeamDTO = modelMapper.map(organizeTeams, OrganizeTeamDTO.class);
        return organizeTeamDTO;
    }

    public TeamSummary mapToTeamSummaryEntity(Object object) {
        TeamSummary teamSummary = modelMapper.map(object, TeamSummary.class);
        return teamSummary;
    }

    public TeamSummaryDTO mapToTeamSummaryDTO(TeamSummary teamSummary) {
        TeamSummaryDTO teamSummaryDTO = modelMapper.map(teamSummary, TeamSummaryDTO.class);
        return teamSummaryDTO;
    }
}

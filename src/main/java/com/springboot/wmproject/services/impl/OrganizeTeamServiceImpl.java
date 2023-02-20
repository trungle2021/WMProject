package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.OrganizeTeamDTO;
import com.springboot.wmproject.entities.OrganizeTeams;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.EmployeeRepository;
import com.springboot.wmproject.repositories.OrderRepository;
import com.springboot.wmproject.repositories.OrganizeTeamRepository;
import com.springboot.wmproject.services.OrganizeTeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizeTeamServiceImpl implements OrganizeTeamService {
    private OrganizeTeamRepository organizeTeamRepository;
    private OrderRepository orderRepository;
    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    @Autowired
    public OrganizeTeamServiceImpl(OrganizeTeamRepository organizeTeamRepository, OrderRepository orderRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.organizeTeamRepository = organizeTeamRepository;
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<OrganizeTeamDTO> getAllOrganizeTeam() {
        return organizeTeamRepository.findAll().stream().map(organizeTeams -> mapToDTO(organizeTeams)).collect(Collectors.toList());
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
    public OrganizeTeamDTO getOneOrganizeTeamById(int id) {
        OrganizeTeams organizeTeams=organizeTeamRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Organize Team","Id",String.valueOf(id)));

        return mapToDTO(organizeTeams);
    }

    @Override
    public OrganizeTeamDTO createOrganizeTeam(OrganizeTeamDTO newOrganizeTeam) throws ResourceNotFoundException{
        String organizeName=newOrganizeTeam.getTeamName();
        if(organizeName!=null){
            List<OrganizeTeams> checkOrganizeTeam=organizeTeamRepository.getOrganizeTeamsByName(organizeName);
            if(checkOrganizeTeam.isEmpty()){
                OrganizeTeams teams=organizeTeamRepository.save(mapToEntity(newOrganizeTeam));
                return mapToDTO(teams);
            }
        }

        return null;
    }

    @Override
    public OrganizeTeamDTO updateOrganizeTeam(OrganizeTeamDTO editOrganizeTeam)throws ResourceNotFoundException {
        int organizeTeamId=editOrganizeTeam.getId();
        OrganizeTeams checkOrganizeTeam=organizeTeamRepository.findById(organizeTeamId).orElseThrow(()->new ResourceNotFoundException("Organize Team","Id",String.valueOf(organizeTeamId)));
        if(checkOrganizeTeam!=null){
            OrganizeTeams organizeTeams=new OrganizeTeams();
            organizeTeams.setId(editOrganizeTeam.getId());
            organizeTeams.setTeamName(editOrganizeTeam.getTeamName());
            organizeTeamRepository.save(organizeTeams);
            return mapToDTO(organizeTeams);
        }
        return null;
    }

    @Override
    public void deleteOrganizeTeam(int id)throws ResourceNotFoundException {
        OrganizeTeams organizeTeams=organizeTeamRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Organize Team","id",String.valueOf(id)));
        organizeTeamRepository.delete(organizeTeams);
    }

    public OrganizeTeams mapToEntity(OrganizeTeamDTO organizeTeamDTO) {
        OrganizeTeams organizeTeams = modelMapper.map(organizeTeamDTO, OrganizeTeams.class);
        return organizeTeams;
    }

    public OrganizeTeamDTO mapToDTO(OrganizeTeams organizeTeams) {
        OrganizeTeamDTO organizeTeamDTO = modelMapper.map(organizeTeams, OrganizeTeamDTO.class);
        return organizeTeamDTO;
    }
}

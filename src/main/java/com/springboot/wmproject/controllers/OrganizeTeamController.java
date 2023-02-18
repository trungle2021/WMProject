package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.OrganizeTeamDTO;
import com.springboot.wmproject.entities.OrganizeTeams;
import com.springboot.wmproject.services.OrganizeTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/teams")
public class OrganizeTeamController {
    private OrganizeTeamService organizeTeamService;
    @Autowired

    public OrganizeTeamController(OrganizeTeamService organizeTeamService) {
        this.organizeTeamService = organizeTeamService;
    }
    @GetMapping(value = "/all")
    public ResponseEntity<List<OrganizeTeamDTO>> getAll(){
        return ResponseEntity.ok(organizeTeamService.getAllOrganizeTeam());
    }
    @GetMapping(value = "/all/name/{name}")
    public ResponseEntity<List<OrganizeTeamDTO>> getAllByName(@PathVariable String name){
        return ResponseEntity.ok(organizeTeamService.searchOrganizeTeamByName(name));
    }
    @GetMapping(value = "/one/id/{id}")
    public ResponseEntity<OrganizeTeamDTO> getAllByName(@PathVariable int id){
        return ResponseEntity.ok(organizeTeamService.getOneOrganizeTeamById(id));
    }
    @PostMapping(value = "/create")
    public ResponseEntity<OrganizeTeamDTO> createOrganizeTeam(@RequestBody OrganizeTeamDTO organizeTeamDTO){
        return new ResponseEntity<>(organizeTeamService.createOrganizeTeam(organizeTeamDTO), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<OrganizeTeamDTO> updateOrganizeTeam(@RequestBody OrganizeTeamDTO organizeTeamDTO){
        return ResponseEntity.ok(organizeTeamService.updateOrganizeTeam(organizeTeamDTO));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrganizeTeam(@PathVariable int id){
        organizeTeamService.deleteOrganizeTeam(id);
        return ResponseEntity.ok("Organize Team has been deleted");
    }
}

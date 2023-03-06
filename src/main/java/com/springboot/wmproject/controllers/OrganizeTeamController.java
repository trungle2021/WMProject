package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.OrganizeTeamDTO;
import com.springboot.wmproject.entities.OrganizeTeams;
import com.springboot.wmproject.services.OrganizeTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/all")
    public ResponseEntity<List<OrganizeTeamDTO>> getAll(){
        return ResponseEntity.ok(organizeTeamService.getAllOrganizeTeam());
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/all/name/{name}")
    public ResponseEntity<List<OrganizeTeamDTO>> getAllByName(@Valid @PathVariable String name){
        return ResponseEntity.ok(organizeTeamService.searchOrganizeTeamByName(name));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/one/id/{id}")
    public ResponseEntity<OrganizeTeamDTO> getAllByName(@Valid @PathVariable int id){
        return ResponseEntity.ok(organizeTeamService.getOneOrganizeTeamById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create")
    public ResponseEntity<OrganizeTeamDTO> createOrganizeTeam(@Valid @RequestBody OrganizeTeamDTO organizeTeamDTO){
        return new ResponseEntity<>(organizeTeamService.createOrganizeTeam(organizeTeamDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update")
    public ResponseEntity<OrganizeTeamDTO> updateOrganizeTeam(@Valid @RequestBody OrganizeTeamDTO organizeTeamDTO){
        return ResponseEntity.ok(organizeTeamService.updateOrganizeTeam(organizeTeamDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOrganizeTeam(@Valid @PathVariable int id){
        organizeTeamService.deleteOrganizeTeam(id);
        return ResponseEntity.ok("Organize Team has been deleted");
    }
}

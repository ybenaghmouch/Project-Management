package com.dxc.solution_intelligente.Controlleur;


import com.dxc.solution_intelligente.DTO.Backlog.BacklogDTO;
import com.dxc.solution_intelligente.DTO.Equipe.EquipeDTO;
import com.dxc.solution_intelligente.DTO.Project.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IProjectService;
import com.dxc.solution_intelligente.service.ProjectService;
import com.dxc.solution_intelligente.service.model.Backlog;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {
    private final IProjectService projectService;

    @GetMapping
    List<ProjectDTO> projects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/search")
    public List<ProjectDTO> searchProjectByName(@RequestParam String name){
        return projectService.findByName(name);
    }

    @GetMapping("/usernameSearch")
    public ResponseEntity<?> searchProjectByUsernameManager(@RequestParam String username) {
        try {
            List<ProjectDTO> projects = projectService.findProjectByManager(username);
            return ResponseEntity.ok(projects);
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Project not found for manager with username: " + username);
        }
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody AddProjectRequest dto){
        try {
            AddProjectResponse response = projectService.createProject(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (BusinessException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur."+e.getMessage());
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<UpdateProjectResponse> updateProject(@PathVariable String name, @RequestBody UpdateProjectRequest dto){
        return new ResponseEntity<>(projectService.updateProject(name, dto), HttpStatus.OK);
    }

    @GetMapping("/backlogs/{nom}")
    public List<Backlog> searchEquipeByNom(@PathVariable String nom) {
        return projectService.findByexactName(nom).getBacklogs();
    }

}

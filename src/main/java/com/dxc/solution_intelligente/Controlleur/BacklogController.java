package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Backlog.*;
import com.dxc.solution_intelligente.DTO.Project.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IBacklogService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/backlog")
public class BacklogController {
    private final IBacklogService backlogService;

    @GetMapping
    List<BacklogDTO> backlogs(){
        return backlogService.getAllBacklogs();
    }

    @GetMapping("/search")
    public List<BacklogDTO> searchBacklogsByTitre(@RequestParam String titre){
        return backlogService.findByTitre(titre);
    }
    @GetMapping("/{titre}")
    public BacklogDTO searchBacklogByTitre(@PathVariable String titre){
        return backlogService.searchByTitre(titre);
    }

    @PostMapping
    public ResponseEntity<?> createBacklog(@RequestBody AddBacklogRequest dto){
        try {
            AddBacklogResponse response = backlogService.createBacklog(dto);
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

    /*@PutMapping("/{name}")
    public ResponseEntity<UpdateProjectResponse> updateProject(@PathVariable String name, @RequestBody UpdateProjectRequest dto){
        return new ResponseEntity<>(projectService.updateProject(name, dto), HttpStatus.OK);
    }*/
    @PutMapping("/{titre}")
    public ResponseEntity<UpdateBacklogResponse> updateBacklog(@PathVariable String titre, @RequestBody UpdateBacklogRequest dto){
        return new ResponseEntity<>(backlogService.updateBacklog(titre, dto), HttpStatus.OK);
    }
    @PostMapping("/{projectName}")
    public ResponseEntity<AddBacklogResponse> addBacklogToProject(@PathVariable String projectName, @RequestBody AddBacklogRequest addBacklogRequest) {
        AddBacklogResponse response = backlogService.addBacklogToProject(projectName, addBacklogRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBacklog(@PathVariable Long id){
        try {
            return new ResponseEntity<>(backlogService.deleteBacklogById(id), HttpStatus.ACCEPTED);
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

}

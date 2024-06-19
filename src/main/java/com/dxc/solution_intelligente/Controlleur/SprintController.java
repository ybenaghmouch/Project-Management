package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Backlog.AddBacklogRequest;
import com.dxc.solution_intelligente.DTO.Backlog.AddBacklogResponse;
import com.dxc.solution_intelligente.DTO.Sprint.UpdateSprintRequest;
import com.dxc.solution_intelligente.DTO.Sprint.UpdateSprintResponse;
import com.dxc.solution_intelligente.DTO.Sprint.AddSprintRequest;
import com.dxc.solution_intelligente.DTO.Sprint.AddSprintResponse;
import com.dxc.solution_intelligente.DTO.Sprint.SprintDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.SprintService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sprint")
public class SprintController {
    private final SprintService sprintService;

    @GetMapping
    List<SprintDTO> sprints(){
        return sprintService.getAllSprints();
    }

    @GetMapping("/search")
    public List<SprintDTO> searchSprintByTitre(@RequestParam String titre){
        return sprintService.findByTitre(titre);
    }

    @PostMapping
    public ResponseEntity<?> createSprint(@RequestBody AddSprintRequest dto){
        try {
            AddSprintResponse response = sprintService.createSprint(dto);
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

    @PutMapping("/{titre}")
    public ResponseEntity<UpdateSprintResponse> updateSprint(@PathVariable String titre, @RequestBody UpdateSprintRequest dto){
        return new ResponseEntity<>(sprintService.updateSprint(titre, dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSprint(@PathVariable Long id){
        try {
            return new ResponseEntity<>(sprintService.deleteSprintById(id), HttpStatus.ACCEPTED);
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

    @PostMapping("/{projectName}")
    public ResponseEntity<AddSprintResponse> addSprintToProject(@PathVariable String projectName, @RequestBody AddSprintRequest addSprintRequest) {
        AddSprintResponse response = sprintService.addSprintToProject(projectName, addSprintRequest);
        return ResponseEntity.ok(response);
    }
}

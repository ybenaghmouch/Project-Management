package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Collaborateur.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.ICollaborateurService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborateur")
@AllArgsConstructor
public class CollaborateurController {
    private final ICollaborateurService collaborateurService;


    @GetMapping()
    List<CollaborateurDTO> collaborateurs(){
        return collaborateurService.getAllCollaborateurs();
    }

    @PostMapping()
    public ResponseEntity<?> createCollaborateur(@RequestBody AddCollaborateurRequest dto){
        try{
        return new ResponseEntity<>(collaborateurService.createCollaborateur(dto), HttpStatus.CREATED);
        } catch (BusinessException e) {
            // Log de l'erreur si nécessaire
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Gestion des autres exceptions inattendues
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur.");
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<UpdateCollaborateurResponse> updateCollaborateur(@PathVariable String username, @RequestBody UpdateCollaborateurRequest dto){
        return new ResponseEntity<>(collaborateurService.updateCollaborateur(username, dto), HttpStatus.OK);
    }


    @GetMapping("/search")
    public List<CollaborateurDTO> searchCollaborateurByUsername(@RequestParam String username) {
        return collaborateurService.findByUsernameContaining(username);
    }


    
}

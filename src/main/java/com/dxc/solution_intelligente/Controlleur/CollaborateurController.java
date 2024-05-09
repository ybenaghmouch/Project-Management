package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.collaborateur.*;
import com.dxc.solution_intelligente.service.ICollaborateurService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collaborator")
@AllArgsConstructor
public class CollaborateurController {
    private final ICollaborateurService collaborateurService;


    @GetMapping()
    List<CollaborateurDTO> collaborateurs(){
        return collaborateurService.getAllCollaborateurs();
    }

    @PostMapping()
    public ResponseEntity<AddCollaborateurResponse> createCollaborateur(@RequestBody AddCollaborateurRequest dto){
        return new ResponseEntity<>(collaborateurService.createCollaborateur(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UpdateCollaborateurResponse> updateCollaborateur(@PathVariable String username, @RequestBody UpdateCollaborateurRequest dto){
        return new ResponseEntity<>(collaborateurService.updateCollaborateur(username, dto), HttpStatus.OK);
    }
    
}

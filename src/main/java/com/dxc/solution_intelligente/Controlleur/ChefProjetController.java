package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.chefProjet.AddChefProjetRequest;
import com.dxc.solution_intelligente.DTO.chefProjet.ChefProjetDTO;
import com.dxc.solution_intelligente.DTO.chefProjet.UpdateChefProjetRequest;
import com.dxc.solution_intelligente.DTO.chefProjet.UpdateChefProjetResponse;
import com.dxc.solution_intelligente.DTO.chefProjet.ChefProjetDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IChefProjetService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chefprojet")
@AllArgsConstructor
public class ChefProjetController {
    private final IChefProjetService chefProjetService;


    @GetMapping()
    List<ChefProjetDTO> chefProjets(){
        return chefProjetService.getAllChefProjets();
    }

    @PostMapping()
    public ResponseEntity<?> createChefProjet(@RequestBody AddChefProjetRequest dto){
        try{
            return new ResponseEntity<>(chefProjetService.createChefProjet(dto), HttpStatus.CREATED);
        } catch (BusinessException e) {
            // Log de l'erreur si nécessaire
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Gestion des autres exceptions inattendues
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur."+e.getMessage() );
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<UpdateChefProjetResponse> updateChefProjet(@PathVariable String username, @RequestBody UpdateChefProjetRequest dto){
        return new ResponseEntity<>(chefProjetService.updateChefProjet(username, dto), HttpStatus.OK);
    }


    @GetMapping("/search")
    public List<ChefProjetDTO> searchChefProjetByUsername(@RequestParam String username) {
        return chefProjetService.findByUsernameContaining(username);
    }
}

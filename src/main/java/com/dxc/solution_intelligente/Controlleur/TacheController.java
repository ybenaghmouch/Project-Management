package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Tache.AddTacheRequest;
import com.dxc.solution_intelligente.DTO.Tache.TacheDTO;
import com.dxc.solution_intelligente.DTO.Tache.UpdateTacheRequest;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.ITacheService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/tache")
@AllArgsConstructor
public class TacheController {
    private final ITacheService tacheService;


    @GetMapping()
    List<TacheDTO> taches(){
        return tacheService.getAllTaches();
    }

    @PostMapping()
    public ResponseEntity<?> createTache(@RequestBody AddTacheRequest dto){
        try{
            return new ResponseEntity<>(tacheService.createTache(dto), HttpStatus.CREATED);
        } catch (BusinessException e) {
            // Log de l'erreur si nécessaire
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Gestion des autres exceptions inattendues
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur."+e.getMessage());
        }
    }

    @PutMapping("/{nom}")
    public ResponseEntity<?> updateTache(@PathVariable String nom, @RequestBody UpdateTacheRequest dto){
        try{
            return new ResponseEntity<>(tacheService.updateTache(nom, dto), HttpStatus.OK);
        } catch (BusinessException e) {
            // Log de l'erreur si nécessaire
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            // Gestion des autres exceptions inattendues
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur."+e.getMessage());
        }
    }


    @GetMapping("/search")
    public List<TacheDTO> searchTacheByNom(@RequestParam String nom) {
        return tacheService.findByCodeAndTitreContaining(nom);
    }
}

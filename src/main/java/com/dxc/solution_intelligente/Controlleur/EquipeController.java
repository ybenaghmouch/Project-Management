package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Equipe.AddEquipeRequest;
import com.dxc.solution_intelligente.DTO.Equipe.EquipeDTO;
import com.dxc.solution_intelligente.DTO.Equipe.UpdateEquipeRequest;
import com.dxc.solution_intelligente.DTO.Equipe.UpdateEquipeResponse;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IEquipeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/team")
@AllArgsConstructor
public class EquipeController {
    private final IEquipeService equipeService;


    @GetMapping()
    List<EquipeDTO> equipes(){
        return equipeService.getAllEquipes();
    }

    @PostMapping()
    public ResponseEntity<?> createEquipe(@RequestBody AddEquipeRequest dto){
        try{
            return new ResponseEntity<>(equipeService.createEquipe(dto), HttpStatus.CREATED);
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
    public ResponseEntity<?> updateEquipe(@PathVariable String nom, @RequestBody UpdateEquipeRequest dto){
        try{
        return new ResponseEntity<>(equipeService.updateEquipe(nom, dto), HttpStatus.OK);
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
    public List<EquipeDTO> searchEquipeByNom(@RequestParam String nom) {
        return equipeService.findByNomContaining(nom);
    }
}

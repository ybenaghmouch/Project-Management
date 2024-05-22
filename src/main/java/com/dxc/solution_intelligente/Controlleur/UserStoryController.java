package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.UserStory.AddUserStoryRequest;
import com.dxc.solution_intelligente.DTO.UserStory.UserStoryDTO;
import com.dxc.solution_intelligente.DTO.UserStory.UpdateUserStoryRequest;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IUserStoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/us")
@AllArgsConstructor
public class UserStoryController {
    private final IUserStoryService userStoryService;


    @GetMapping()
    List<UserStoryDTO> userStorys(){
        return userStoryService.getAllUserStorys();
    }

    @PostMapping()
    public ResponseEntity<?> createUserStory(@RequestBody AddUserStoryRequest dto){
        try{
            return new ResponseEntity<>(userStoryService.createUserStory(dto), HttpStatus.CREATED);
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
    public ResponseEntity<?> updateUserStory(@PathVariable String nom, @RequestBody UpdateUserStoryRequest dto){
        try{
            return new ResponseEntity<>(userStoryService.updateUserStory(nom, dto), HttpStatus.OK);
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
    public List<UserStoryDTO> searchUserStoryByNom(@RequestParam String nom) {
        return userStoryService.findByCodeAndTitreContaining(nom);
    }
}

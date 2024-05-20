package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Manager.ManagerDTO;
import com.dxc.solution_intelligente.DTO.Manager.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    private final IManagerService managerService;

    public ManagerController(IManagerService managerService){
        this.managerService=managerService;
    }

    @GetMapping()
    List<ManagerDTO> managers(){
        return managerService.getAllManagers();
    }

    @PostMapping()
    public ResponseEntity<?> createManager(@RequestBody AddManagerRequest dto) {
        try {
            AddManagerResponse response = managerService.createManager(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur."+e.getMessage());
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<UpdateManagerResponse> updateManager(@PathVariable String username, @RequestBody UpdateManagerRequest dto){
        return new ResponseEntity<>(managerService.updateManager(username, dto), HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<ManagerDTO> searchManagerByUsername(@RequestParam String username) {
        return managerService.findByUsernameContaining(username);
    }
}

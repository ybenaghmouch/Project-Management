package com.dxc.solution_intelligente.Controlleur;


import com.dxc.solution_intelligente.DTO.Permission.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IPermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
@AllArgsConstructor
public class PermissionController {
    private final IPermissionService permissionService;

    @GetMapping()
    List<PermissionDTO> permissions(){
        return permissionService.getAllPermissions();
    }

    @PostMapping()
    public ResponseEntity<?> createPermission(@RequestBody AddPermissionRequest dto) {
        try {
            AddPermissionResponse response = permissionService.createPermission(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdatePermissionResponse> updatePermission(@PathVariable Long id, @RequestBody UpdatePermissionRequest dto){
        return new ResponseEntity<>(permissionService.updatePermission(id, dto), HttpStatus.OK);
    }

}

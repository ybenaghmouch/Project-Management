package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Permission.*;
import com.dxc.solution_intelligente.DTO.Role.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@AllArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping()
    List<RoleDTO> roles(){
        return roleService.getAllRoles();
    }

    @PostMapping()
    public ResponseEntity<?> createRole(@RequestBody AddRoleRequest dto) {
        try {
            AddRoleResponse response = roleService.createRole(dto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (BusinessException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur." + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateRoleResponse> updateRole(@PathVariable String authority, @RequestBody UpdateRoleRequest dto){
        return new ResponseEntity<>(roleService.updateRole(authority, dto), HttpStatus.OK);
    }
}

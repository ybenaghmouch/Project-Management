package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Admin.*;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final IAdminService adminService;

    public AdminController(IAdminService adminService){
        this.adminService=adminService;
    }


    @GetMapping()
    List<AdminDTO> admins(){
        return adminService.getAllAdmins();
    }


   /* @PostMapping("/add")
    public ResponseEntity<AddAdminResponse> createAdmin(@RequestBody AddAdminRequest dto){
        return new ResponseEntity<>(adminService.createAdmin(dto), HttpStatus.CREATED);
    }*/
   @PostMapping()
   public ResponseEntity<?> createAdmin(@RequestBody AddAdminRequest dto) {
       try {
           AddAdminResponse response = adminService.createAdmin(dto);
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


    @PutMapping("/{username}")
    public ResponseEntity<UpdateAdminResponse> updateAdmin(@PathVariable String username, @RequestBody UpdateAdminRequest dto){
        return new ResponseEntity<>(adminService.updateAdmin(username, dto), HttpStatus.OK);
    }
}

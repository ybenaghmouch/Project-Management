package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.User.*;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final IUserService userService;


    @GetMapping()
    List<UserDTO> users(){
        return userService.getAllUsers();
    }

    @GetMapping("/search")
    public List<UserDTO> searchUserByUsername(@RequestParam String username) {
        return userService.findByUsernameContaining(username);
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody AddUserRequest dto) {
        try {
            AddUserResponse response = userService.createUser(dto);
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
    public ResponseEntity<UpdateUserResponse> updateUser(@PathVariable String username, @RequestBody UpdateUserRequest dto){
        return new ResponseEntity<>(userService.updateUser(username, dto), HttpStatus.OK);
    }
}

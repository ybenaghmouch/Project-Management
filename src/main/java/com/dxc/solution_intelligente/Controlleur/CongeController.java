package com.dxc.solution_intelligente.Controlleur;

import com.dxc.solution_intelligente.DTO.Conge.*;
import com.dxc.solution_intelligente.DTO.Equipe.EquipeDTO;
import com.dxc.solution_intelligente.service.CongeService;
import com.dxc.solution_intelligente.service.ICongeService;
import com.dxc.solution_intelligente.service.model.Conge;
import com.dxc.solution_intelligente.service.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/conges")

public class CongeController {


    private final ICongeService congeService;

    @GetMapping()
    List<CongeDTO> equipes(){
        return congeService.getAllConges();
    }

    @PostMapping()
    public AddCongeResponse createConge(@RequestBody AddCongeRequest addCongeRequest) {
        return congeService.createConge(addCongeRequest);
    }

    @PutMapping("/{id}")
    public UpdateCongeResponse updateConge(@PathVariable Long id, @RequestBody UpdateCongeRequest updateCongeRequest) {
        return congeService.updateConge(id, updateCongeRequest);
    }


}

package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.CongeRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.Conge.*;
import com.dxc.solution_intelligente.DTO.User.UserDTO;
import com.dxc.solution_intelligente.service.Exception.BusinessException;
import com.dxc.solution_intelligente.service.model.Conge;
import com.dxc.solution_intelligente.service.model.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@AllArgsConstructor
public class CongeService implements ICongeService {
    private final ModelMapper modelMapper;
    private final CongeRepository congeRepository;
    private final UserRepository userRepository;

    @Override
    public List<CongeDTO> getAllConges() {
        return congeRepository.findAll().stream()
                .map(conge -> modelMapper.map(conge, CongeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddCongeResponse createConge(AddCongeRequest addCongeRequest) {
        User demandeur = userRepository.findById(addCongeRequest.getDemandeur().getId())
                .orElseThrow(() -> new BusinessException("Demandeur not found"));
        User backup = userRepository.findById(addCongeRequest.getBackup().getId())
                .orElseThrow(() -> new BusinessException("Backup not found"));

        // Check if the backup is already assigned to another Conge with allowed or pending status
        checkBackupAvailability(backup, null);

        // Check if the demandeur has sufficient soldeConge
        checkDemandeurSoldeConge(demandeur, addCongeRequest.getDuration());

        Conge conge = new Conge();
        conge.setDemandeur(demandeur);
        conge.setBackup(backup);
        conge.setDuration(addCongeRequest.getDuration());
        conge.setMotif(addCongeRequest.getMotif());
        conge.setFromDate(addCongeRequest.getFromDate());
        conge.setEndDate(addCongeRequest.getEndDate());
        conge.setStatus(addCongeRequest.getStatus());

        // Save the conge
        Conge savedConge = congeRepository.save(conge);

        // Update demandeur's soldeConge if the status is "allowed"
        if ("allowed".equals(savedConge.getStatus())) {
            demandeur.setSoldeConge(demandeur.getSoldeConge() - savedConge.getDuration());
            userRepository.save(demandeur);
        }

        // Prepare response
        AddCongeResponse response = new AddCongeResponse();
       // response.setId(savedConge.getId());
        response.setDemandeur(modelMapper.map(demandeur, UserDTO.class));
        response.setBackup(modelMapper.map(backup, UserDTO.class));
        response.setDuration(savedConge.getDuration());
        response.setMotif(savedConge.getMotif());
        response.setFromDate(savedConge.getFromDate());
        response.setEndDate(savedConge.getEndDate());
        response.setStatus(savedConge.getStatus());

        response.setMessage(String.format("Conge : [Nom = %s]", demandeur.getFirstName()));

        return response;
    }

    @Override
    public UpdateCongeResponse updateConge(Long id, UpdateCongeRequest updateCongeRequest) {
        Conge congeFound = congeRepository.findById(id).orElseThrow(
                () -> new BusinessException(String.format("Conge avec l'id [%d] n'existe pas!", id))
        );

        User demandeur = userRepository.findById(updateCongeRequest.getDemandeur().getId())
                .orElseThrow(() -> new BusinessException("Demandeur not found"));
        User backup = userRepository.findById(updateCongeRequest.getBackup().getId())
                .orElseThrow(() -> new BusinessException("Backup not found"));

        // Check if the backup is already assigned to another Conge with allowed or pending status, excluding the current Conge
        checkBackupAvailability(backup, congeFound);

        // Check if the demandeur has sufficient soldeConge for the new duration if status is "allowed"
        if ("allowed".equals(updateCongeRequest.getStatus()) && !updateCongeRequest.getStatus().equals(congeFound.getStatus())) {
            int additionalDays = updateCongeRequest.getDuration() - congeFound.getDuration();
            checkDemandeurSoldeConge(demandeur, additionalDays);
            demandeur.setSoldeConge(demandeur.getSoldeConge() - additionalDays);
            userRepository.save(demandeur);
        }

        congeFound.setDemandeur(demandeur);
        congeFound.setBackup(backup);
        congeFound.setDuration(updateCongeRequest.getDuration());
        congeFound.setMotif(updateCongeRequest.getMotif());
        congeFound.setFromDate(updateCongeRequest.getFromDate());
        congeFound.setEndDate(updateCongeRequest.getEndDate());
        congeFound.setStatus(updateCongeRequest.getStatus());

        Conge savedConge = congeRepository.save(congeFound);

        // Prepare response
        UpdateCongeResponse updateCongeResponse = new UpdateCongeResponse();
      //  updateCongeResponse.setId(savedConge.getId());
        updateCongeResponse.setDemandeur(modelMapper.map(demandeur, UserDTO.class));
        updateCongeResponse.setBackup(modelMapper.map(backup, UserDTO.class));
        updateCongeResponse.setDuration(savedConge.getDuration());
        updateCongeResponse.setMotif(savedConge.getMotif());
        updateCongeResponse.setFromDate(savedConge.getFromDate());
        updateCongeResponse.setEndDate(savedConge.getEndDate());
        updateCongeResponse.setStatus(savedConge.getStatus());

        updateCongeResponse.setMessage(String.format("Conge avec l'id [%d] a ete modifie avec succes !", id));

        return updateCongeResponse;
    }


    private void checkBackupAvailability(User backup, Conge currentConge) {
        List<Conge> conflictingConges = congeRepository.findByBackup(backup);
        Optional<Conge> conflictingConge = conflictingConges.stream()
                .filter(conge -> ("allowed".equals(conge.getStatus()) || "pending".equals(conge.getStatus()))
                        && conge.getEndDate().after(new Date())
                        && (currentConge == null || !conge.getId().equals(currentConge.getId())))
                .findFirst();

        if (conflictingConge.isPresent()) {
            throw new BusinessException(String.format("Backup [%s] is already assigned to another Conge", backup.getFirstName()));
        }
    }

    private void checkDemandeurSoldeConge(User demandeur, int duration) {
        if (demandeur.getSoldeConge() < duration) {
            throw new BusinessException(String.format("Insufficient soldeConge for Demandeur [%s]", demandeur.getFirstName()));
        }
    }
}

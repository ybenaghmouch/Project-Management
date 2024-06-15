package com.dxc.solution_intelligente.service;

import com.dxc.solution_intelligente.DAO.CongeRepository;
import com.dxc.solution_intelligente.DAO.UserRepository;
import com.dxc.solution_intelligente.DTO.Conge.*;
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
@Transactional
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

    public AddCongeResponse createConge(AddCongeRequest addCongeRequest) {
        Conge conge = modelMapper.map(addCongeRequest, Conge.class);

        User demandeur = userRepository.findById(conge.getDemandeur().getId())
                .orElseThrow(() -> new BusinessException("Demandeur not found"));
        User backup = userRepository.findById(conge.getBackup().getId())
                .orElseThrow(() -> new BusinessException("Backup not found"));

        // Check if the backup is already assigned to another Conge with allowed or pending status
        checkBackupAvailability(backup, null);

        // Check if the demandeur has sufficient soldeConge
        checkDemandeurSoldeConge(demandeur, conge.getDuration());

        conge.setDemandeur(demandeur);
        conge.setBackup(backup);

        // Save the conge
        AddCongeResponse response = modelMapper.map(congeRepository.save(conge), AddCongeResponse.class);

        // Update demandeur's soldeConge if the status is "allowed"
        if ("allowed".equals(conge.getStatus())) {
            demandeur.setSoldeConge(demandeur.getSoldeConge() - conge.getDuration());
            userRepository.save(demandeur);
        }

        // Set response message and related users
        response.setMessage(String.format("Conge : [Nom = %s]", demandeur.getFirstName()));
        response.setDemandeur(demandeur);
        response.setBackup(backup);
        return response;
    }

    public UpdateCongeResponse updateConge(Long id, UpdateCongeRequest updateCongeRequest) {
        Conge congeToPersist = modelMapper.map(updateCongeRequest, Conge.class);
        Conge congeFound = congeRepository.findById(id).orElseThrow(
                () -> new BusinessException(String.format("Conge avec l'id [%d] n'existe pas!", id))
        );

        User demandeur = userRepository.findById(congeToPersist.getDemandeur().getId())
                .orElseThrow(() -> new BusinessException("Demandeur not found"));
        User backup = userRepository.findById(congeToPersist.getBackup().getId())
                .orElseThrow(() -> new BusinessException("Backup not found"));

        // Check if the backup is already assigned to another Conge with allowed or pending status, excluding the current Conge
        checkBackupAvailability(backup, congeFound);

        // Check if the demandeur has sufficient soldeConge for the new duration if status is "allowed"
        if ("allowed".equals(congeToPersist.getStatus()) && !congeToPersist.getStatus().equals(congeFound.getStatus())) {
            int additionalDays = congeToPersist.getDuration();
            checkDemandeurSoldeConge(demandeur, additionalDays);
            demandeur.setSoldeConge(demandeur.getSoldeConge() - additionalDays);
            userRepository.save(demandeur);
        }

        congeToPersist.setId(congeFound.getId());
        congeToPersist.setDemandeur(demandeur);
        congeToPersist.setBackup(backup);

        // Update conge details
        UpdateCongeResponse updateCongeResponse = modelMapper.map(congeRepository.save(congeToPersist), UpdateCongeResponse.class);
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

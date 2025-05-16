package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.UtilisateurDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.model.Utilisateur;
import com.k48.stock_management_system.repositories.UtilisateurRepository;
import com.k48.stock_management_system.services.UtilisateurService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilissateurServiceImpl  implements UtilisateurService {

    private final ObjectValidator<UtilisateurDto> objectValidator;
    private final UtilisateurRepository utilisateurRepository;


    @Override
    public UtilisateurDto save(UtilisateurDto utilisateurDto) {
        objectValidator.validate(utilisateurDto);

        return UtilisateurDto.toDto(utilisateurRepository.save(UtilisateurDto.toEntity(utilisateurDto)));
    }

    @Override
    public UtilisateurDto findById(Integer id) {
        if(id == null) {
            return null;
        }

        return
                utilisateurRepository
                        .findById(id)
                        .map(UtilisateurDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Utilisateur id " + id + " not found!")
                        );
    }

    @Override
    public List<UtilisateurDto> findAll() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        return Optional.of(utilisateurs).filter(list-> !list.isEmpty()).orElseThrow(
                ()-> new EntityNotFoundException("EMPTY List")
        ).stream().map(UtilisateurDto::toDto).toList();
    }

    @Override
    public UtilisateurDto delete(Integer id) {
        if(id == null) {
            return null;
        }

        Utilisateur utilisateur =
                utilisateurRepository
                        .findById(id)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Utilisateur id " + id + " not found!")
                        );
        utilisateurRepository.delete(utilisateur);
        return UtilisateurDto.toDto(utilisateur);
    }
}

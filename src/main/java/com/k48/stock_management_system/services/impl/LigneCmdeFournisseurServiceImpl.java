package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.model.LigneCmdeFournisseur;
import com.k48.stock_management_system.repositories.LigneCmdeFournisseurRepository;
import com.k48.stock_management_system.services.LigneCmdeFournisseurService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LigneCmdeFournisseurServiceImpl implements LigneCmdeFournisseurService {

    private final LigneCmdeFournisseurRepository repository;
    private final ObjectValidator<LigneCmdeFournisseurDto> objectValidator;

    @Override
    public LigneCmdeFournisseurDto save(LigneCmdeFournisseurDto dto) {
        objectValidator.validate(dto);
        return LigneCmdeFournisseurDto.fromEntity(repository.save(LigneCmdeFournisseurDto.toEntity(dto)));
    }

    @Override
    public LigneCmdeFournisseurDto findById(Integer id) {
        if (id == null) {
            log.error("LigneCmdeFournisseur ID is null");
            return null;
        }
        return repository.findById(id)
                .map(LigneCmdeFournisseurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Ligne commande fournisseur avec l'ID " + id + " non trouvée",
                    ErrorCode.LIGNE_CMDE_FOURNISSEUR_NOT_FOUND
                ));
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAll() {
        List<LigneCmdeFournisseur> lignes = repository.findAll();
        return Optional.of(lignes)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Aucune ligne de commande fournisseur trouvée",
                    ErrorCode.EMPTY_LIST
                ))
                .stream()
                .map(LigneCmdeFournisseurDto::fromEntity)
                .toList();
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAllByCommandeFournisseurId(Integer commandeFournisseurId) {
        return repository.findAllByCmdeFournisseurId(commandeFournisseurId)
                .stream()
                .map(LigneCmdeFournisseurDto::fromEntity)
                .toList();
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAllByArticleId(Integer articleId) {
        return repository.findAllByArticleId(articleId)
                .stream()
                .map(LigneCmdeFournisseurDto::fromEntity)
                .toList();
    }

    @Override
    public LigneCmdeFournisseurDto delete(Integer id) {
        if (id == null) {
            log.error("LigneCmdeFournisseur ID is null");
            return null;
        }
        LigneCmdeFournisseur ligne = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Ligne commande fournisseur avec l'ID " + id + " non trouvée",
                    ErrorCode.LIGNE_CMDE_FOURNISSEUR_NOT_FOUND
                ));
        
        repository.delete(ligne);
        return LigneCmdeFournisseurDto.fromEntity(ligne);
    }
}
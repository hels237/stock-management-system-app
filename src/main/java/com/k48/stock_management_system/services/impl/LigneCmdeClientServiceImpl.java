package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.model.LigneCmdeClient;
import com.k48.stock_management_system.repositories.LigneCmdeClientRepository;
import com.k48.stock_management_system.services.LigneCmdeClientService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LigneCmdeClientServiceImpl implements LigneCmdeClientService {

    private final LigneCmdeClientRepository repository;
    private final ObjectValidator<LigneCmdeClientDto> objectValidator;

    @Override
    public LigneCmdeClientDto save(LigneCmdeClientDto dto) {
        objectValidator.validate(dto);
        return LigneCmdeClientDto.fromEntity(repository.save(LigneCmdeClientDto.toEntity(dto)));
    }

    @Override
    public LigneCmdeClientDto findById(Integer id) {
        if (id == null) {
            log.error("LigneCmdeClient ID is null");
            return null;
        }
        return repository.findById(id)
                .map(LigneCmdeClientDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Ligne commande client avec l'ID " + id + " non trouvée",
                    ErrorCode.lIGNE_CMDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public List<LigneCmdeClientDto> findAll() {
        List<LigneCmdeClient> lignes = repository.findAll();
        return Optional.of(lignes)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Aucune ligne de commande client trouvée",
                    ErrorCode.EMPTY_LIST
                ))
                .stream()
                .map(LigneCmdeClientDto::fromEntity)
                .toList();
    }

    @Override
    public List<LigneCmdeClientDto> findAllByCommandeClientId(Integer commandeClientId) {
        return repository.findAllByCmdeClientId(commandeClientId)
                .stream()
                .map(LigneCmdeClientDto::fromEntity)
                .toList();
    }

    @Override
    public List<LigneCmdeClientDto> findAllByArticleId(Integer articleId) {
        return repository.findAllByArticleId(articleId)
                .stream()
                .map(LigneCmdeClientDto::fromEntity)
                .toList();
    }

    @Override
    public LigneCmdeClientDto delete(Integer id) {
        if (id == null) {
            log.error("LigneCmdeClient ID is null");
            return null;
        }
        LigneCmdeClient ligne = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Ligne commande client avec l'ID " + id + " non trouvée",
                    ErrorCode.lIGNE_CMDE_CLIENT_NOT_FOUND
                ));
        
        repository.delete(ligne);
        return LigneCmdeClientDto.fromEntity(ligne);
    }
}
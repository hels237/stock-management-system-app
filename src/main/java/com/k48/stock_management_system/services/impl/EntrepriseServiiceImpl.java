package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.EntrepriseDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.model.Entreprise;
import com.k48.stock_management_system.repositories.EntrepriseRepository;
import com.k48.stock_management_system.services.EntrepriseService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EntrepriseServiiceImpl implements EntrepriseService {

    private final EntrepriseRepository  entrepriseRepository;
    private final ObjectValidator<EntrepriseDto> objectValidator;


    @Override
    public EntrepriseDto save(EntrepriseDto entrepriseDto) {
        objectValidator.validate(entrepriseDto);
        return EntrepriseDto.toDto(entrepriseRepository.save(EntrepriseDto.toEntity(entrepriseDto)));
    }

    @Override
    public EntrepriseDto findById(Integer id) {
        if(id == null) {
            return null;
        }
        return
                entrepriseRepository
                        .findById(id)
                        .map(EntrepriseDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Entreprise with id " + id + " not found")
                        );
    }

    @Override
    public List<EntrepriseDto> findAll() {
        List<Entreprise> entreprises =  entrepriseRepository.findAll();
        return
                Optional.of(entreprises)
                        .filter(list-> !list.isEmpty())
                        .orElseThrow(
                                ()-> new EntityNotFoundException("EMPTY LIST")
                        )
                        .stream()
                        .map(EntrepriseDto::toDto)
                        .toList();
    }

    @Override
    public EntrepriseDto delete(Integer id) {
        if (id == null) {
            return null;
        }
        Entreprise entreprise =
                entrepriseRepository
                        .findById(id)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Entreprise with id " + id + " not found")
                        );
        entrepriseRepository.delete(entreprise);
        return EntrepriseDto.toDto(entreprise);
    }
}

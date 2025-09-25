package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.EntrepriseDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.model.Entreprise;
import com.k48.stock_management_system.repositories.EntrepriseRepository;
import com.k48.stock_management_system.services.EntrepriseService;
import com.k48.stock_management_system.services.UtilisateurService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EntrepriseServiiceImpl implements EntrepriseService {

    private final EntrepriseRepository  entrepriseRepository;
    private final UtilisateurService utilisateurService;
    private final ObjectValidator<EntrepriseDto> objectValidator;


    @Override
    public EntrepriseDto save(EntrepriseDto entrepriseDto) {
        objectValidator.validate(entrepriseDto);
        
        // Vérifier l'unicité par email
        if (entrepriseDto.getEmail() != null) {
            Optional<Entreprise> existingByEmail = entrepriseRepository.findByEmail(entrepriseDto.getEmail());
            if (existingByEmail.isPresent()) {
                log.error("Une entreprise avec l'email{} existe déjà", entrepriseDto.getEmail());
                throw new InvalidOperationException(
                    "Une entreprise avec cet email existe déjà",
                    ErrorCode.ENTREPRISE_ALREADY_EXISTS
                );
            }
        }
        
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
            log.error("Entreprise ID is null");
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

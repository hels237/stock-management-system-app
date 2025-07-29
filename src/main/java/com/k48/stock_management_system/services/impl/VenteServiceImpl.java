package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.VenteDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.model.Vente;
import com.k48.stock_management_system.repositories.VenteRepository;
import com.k48.stock_management_system.services.VenteService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;
    private final ObjectValidator<VenteDto> objectValidator;

    @Override
    public VenteDto save(VenteDto venteDto) {
        objectValidator.validate(venteDto);
        return VenteDto.toDto(venteRepository.save(VenteDto.toEntity(venteDto)));
    }

    @Override
    public VenteDto findById(Integer id) {
        if(id == null) {
            return null;
        }
        return
                venteRepository
                        .findById(id)
                        .map(VenteDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Vente with id " + id + " not found")
                        );
    }

    @Override
    public VenteDto findByCode(String code) {
        return null;
    }

    @Override
    public List<VenteDto> findAll() {

        List<Vente> ventes = venteRepository.findAll();

        return Optional.of(ventes)
                .filter(list-> !list.isEmpty())
                .orElseThrow(
                        ()-> new EntityNotFoundException("EMPTY LIST")
                ).stream()
                .map(VenteDto::toDto)
                .toList();
    }

    @Override
    public VenteDto delete(Integer id) {
        if(id == null) {
            return null;
        }
        Vente vente =
                venteRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Vente with id " + id + " not found")
                        );
        venteRepository.delete(vente);
        return VenteDto.toDto(vente);
    }
}

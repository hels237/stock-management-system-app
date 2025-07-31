package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.FournisseurDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.model.Fournisseur;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.repositories.FournisseurRepository;
import com.k48.stock_management_system.repositories.LigneCmdeFournisseurRepository;
import com.k48.stock_management_system.services.FournisseurService;
import com.k48.stock_management_system.services.MvtStockService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FournisseurServiceImpl implements FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final ObjectValidator<FournisseurDto> objectValidator;
    private final LigneCmdeFournisseurRepository ligneCommandeFournisseurRepository;
    private final ArticleRepository articleRepository;
    private final MvtStockService mvtStkService;

    @Override
    public FournisseurDto save(FournisseurDto fournisseurDto) {

        objectValidator.validate(fournisseurDto);

        return FournisseurDto.fromEntity(fournisseurRepository.save(FournisseurDto.toEntity(fournisseurDto)));
    }

    @Override
    public FournisseurDto findById(Integer id) {
        if(id == null) {
            return null;
        }
        return
                fournisseurRepository
                        .findById(id)
                        .map(FournisseurDto::fromEntity)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Fournisseur with id " + id + " not found")
                        );
    }

    @Override
    public List<FournisseurDto> findAll() {

        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();

        return Optional.of(fournisseurs)
                .filter(list-> !list.isEmpty())
                .orElseThrow(
                        ()-> new EntityNotFoundException("EMPTY LIST ")
                )
                .stream()
                .map(FournisseurDto::fromEntity)
                .toList();
    }

    @Override
    public FournisseurDto delete(Integer id) {

        if(id == null) {
            return null;
        }
        Fournisseur fournisseur = fournisseurRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Fournisseur with id " + id + " not found"));
        fournisseurRepository.delete(fournisseur);

        return FournisseurDto.fromEntity(fournisseur);
    }
}

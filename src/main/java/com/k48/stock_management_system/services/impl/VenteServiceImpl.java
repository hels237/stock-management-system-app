package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.dto.LigneVenteDto;
import com.k48.stock_management_system.dto.MvtStockDto;
import com.k48.stock_management_system.dto.VenteDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.model.*;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.repositories.LigneVenteRepository;
import com.k48.stock_management_system.repositories.VenteRepository;
import com.k48.stock_management_system.services.MvtStockService;
import com.k48.stock_management_system.services.VenteService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;
    private final ObjectValidator<VenteDto> objectValidator;
    private final LigneVenteRepository ligneVenteRepository;
    private final ArticleRepository articleRepository;
    private final MvtStockService mvtStockService;

    @Override
    public VenteDto save(VenteDto venteDto) {

        objectValidator.validate(venteDto);

        venteDto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticleDto().getId());
            if (article.isEmpty()) {
                log.error("Aucun article avec l'ID " + ligneVenteDto.getArticleDto().getId() + " n'a ete trouve dans la BD");
            }
        });

        Vente savedVentes = venteRepository.save(VenteDto.toEntity(venteDto));

        venteDto.getLigneVentes().forEach(ligneVenteDto -> {
            LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
            ligneVente.setVente(savedVentes);
            ligneVenteRepository.save(ligneVente);
            updateMvtStk(ligneVente);
        });

        return VenteDto.fromEntity(venteRepository.save(VenteDto.toEntity(venteDto)));
    }

    @Override
    public VenteDto findById(Integer id) {
        if(id == null) {
            log.error("Ventes n'est pas valide");
            return null;
        }
        return
                venteRepository
                        .findById(id)
                        .map(VenteDto::fromEntity)
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
                .map(VenteDto::fromEntity)
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
        return VenteDto.fromEntity(vente);
    }

    private void updateMvtStk(LigneVente lig) {
        MvtStockDto mvtStkDto = MvtStockDto.builder()
                .articleDto(ArticleDto.fromEntity(lig.getArticle()))
                .dateMvt(Instant.now())
                .typeMvt(TypeMvtStock.SORTIE)
                .sourceMvt(SourceMvtStock.VENTE)
                .quantite(lig.getQuantite())
                .entrepriseId(lig.getIdEntreprise())
                .build();
        mvtStockService.sortieStock(mvtStkDto);
    }
}

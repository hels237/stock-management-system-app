package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.LigneVenteDto;
import com.k48.stock_management_system.dto.VenteDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.model.LigneVente;
import com.k48.stock_management_system.model.Vente;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.repositories.VenteRepository;
import com.k48.stock_management_system.services.VenteService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VenteServiceImpl implements VenteService {

    private final VenteRepository venteRepository;
    private final ObjectValidator<VenteDto> objectValidator;
    private final ArticleRepository articleRepository;

    @Override
    public VenteDto save(VenteDto venteDto) {

        objectValidator.validate(venteDto);

        venteDto.getLigneVentes().forEach(ligneVenteDto -> {
            Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
            if (article.isEmpty()) {
                log.error("Aucun article avec l'ID " + ligneVenteDto.getArticle().getId() + " n'a ete trouve dans la BD");
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
}

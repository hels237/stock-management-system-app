package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.CmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.model.*;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.repositories.CmdeFournisseurRepository;
import com.k48.stock_management_system.repositories.FournisseurRepository;
import com.k48.stock_management_system.repositories.LigneCmdeFournisseurRepository;
import com.k48.stock_management_system.services.CmdeFournisseurService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CmdeFournisseurServiceImpl implements CmdeFournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final ArticleRepository articleRepository;
    private final CmdeFournisseurRepository cmdeFournisseurRepository;
    private final ObjectValidator<CmdeFournisseurDto> objectValidator;
    private final LigneCmdeFournisseurRepository ligneCmdeFournisseurRepository;

    @Override
    public CmdeFournisseurDto save(CmdeFournisseurDto cmdFournisseurDto) {

        // validate the cmdeFournisseurDto
        objectValidator.validate(cmdFournisseurDto);

        // verify if the supplier is present in DB
        Fournisseur supplier =
                fournisseurRepository
                        .findById(cmdFournisseurDto.getFournisseurDto().getId())
                        .orElseThrow(()-> new EntityNotFoundException("supplier not found "));

        // verify that the article is present for each lineCmdFournisseur
        if(cmdFournisseurDto.getLigneCmdeFournisseurDtos() != null) {
            cmdFournisseurDto.getLigneCmdeFournisseurDtos().forEach(ligcmdf -> {

                if(ligcmdf.getArticleDto() != null) {
                    Article article =
                            articleRepository
                                    .findById(ligcmdf.getArticleDto().getId())
                                    .orElseThrow(
                                            ()-> new EntityNotFoundException("article not found")
                                    );
                }
            });

        }else {
            log.warn("No Article found in DB we can't finalize the save operation");
        }

        // save the cmdeFournisseur
        CmdeFournisseur cmdSaved = cmdeFournisseurRepository.save(CmdeFournisseurDto.toEntity(cmdFournisseurDto));

        // save the LineCmdeFournisseur
        if(cmdFournisseurDto.getLigneCmdeFournisseurDtos() != null) {
            cmdFournisseurDto.getLigneCmdeFournisseurDtos().forEach(ligcmdf -> {
                LigneCmdeFournisseur ligneCmdeFournisseur = LigneCmdeFournisseurDto.toEntity(ligcmdf);
                ligneCmdeFournisseur.setCmdeFournisseur(cmdSaved);
                ligneCmdeFournisseurRepository.save(ligneCmdeFournisseur);
            });
        }
        //
        return CmdeFournisseurDto.toDto(cmdSaved);
    }

    @Override
    public CmdeFournisseurDto updateEtatCommande(Integer idCommande, EtatCmde etatCommande) {
        return null;
    }

    @Override
    public CmdeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        return null;
    }

    @Override
    public CmdeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        return null;
    }

    @Override
    public CmdeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        return null;
    }

    @Override
    public CmdeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        return null;
    }

    @Override
    public CmdeFournisseurDto findById(Integer id) {
        if(id == null) {
            log.error("No ID was provided");
            return null;
        }
        return
                cmdeFournisseurRepository
                .findById(id)
                        .map(CmdeFournisseurDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException(" cmdFournisseur not found with the ID {}")
                        );
    }

    @Override
    public CmdeFournisseurDto findByCode(String code) {
        return null;
    }

    @Override
    public List<CmdeFournisseurDto> findAll() {

        List<CmdeFournisseur> cmdeFournisseurs = cmdeFournisseurRepository.findAll();
        return
                Optional.of(cmdeFournisseurs)
                        .filter(cmdlist-> !cmdlist.isEmpty())
                        .orElseThrow(
                                ()-> new EntityNotFoundException("EMPTY LIST {}")
                        ).stream()
                        .map(CmdeFournisseurDto::toDto)
                        .toList();
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
        return List.of();
    }

    @Override
    public CmdeFournisseurDto delete(Integer id) {
        if(id == null) {
            log.error("No ID was provided");
            return null;
        }
        CmdeFournisseur cmdeFournisseur =
                cmdeFournisseurRepository
                        .findById(id)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("cmdeFournisseur not found with the ID {}")
                        );
        cmdeFournisseurRepository.delete(cmdeFournisseur);

        return CmdeFournisseurDto.toDto(cmdeFournisseur);
    }
}

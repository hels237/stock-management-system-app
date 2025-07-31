package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.*;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.model.*;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.repositories.CmdeFournisseurRepository;
import com.k48.stock_management_system.repositories.FournisseurRepository;
import com.k48.stock_management_system.repositories.LigneCmdeFournisseurRepository;
import com.k48.stock_management_system.services.CmdeFournisseurService;
import com.k48.stock_management_system.services.MvtStockService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CmdeFournisseurServiceImpl implements CmdeFournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final ArticleRepository articleRepository;
    private final CmdeFournisseurRepository cmdeFournisseurRepository;
    private final ObjectValidator<CmdeFournisseurDto> objectValidator;
    private final LigneCmdeFournisseurRepository ligneCmdeFournisseurRepository;
    private MvtStockService mvtStkService;
    private final ObjectValidator<ArticleDto> objectValidatorArticleDto;

    @Override
    public CmdeFournisseurDto save(CmdeFournisseurDto cmdFournisseurDto) {

        // validate the cmdeFournisseurDto
        objectValidator.validate(cmdFournisseurDto);

        // verifie si la commande est deja livree
        if (cmdFournisseurDto.getId() != null && cmdFournisseurDto.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }

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
                                            ()-> new EntityNotFoundException("article with ID "+ligcmdf.getArticleDto().getId()+" not found")
                                    );
                }
            });

        }else {
            log.warn("No Article found in DB we can't finalize the save operation");
        }



        cmdFournisseurDto.setDateCmde(Instant.now());
        // save the cmdeFournisseur
        CmdeFournisseur cmdSaved = cmdeFournisseurRepository.save(CmdeFournisseurDto.toEntity(cmdFournisseurDto));

        // save the LineCmdeFournisseur
        if(cmdFournisseurDto.getLigneCmdeFournisseurDtos() != null) {
            cmdFournisseurDto.getLigneCmdeFournisseurDtos().forEach(ligcmdf -> {
                LigneCmdeFournisseur ligneCmdeFournisseur = LigneCmdeFournisseurDto.toEntity(ligcmdf);
                ligneCmdeFournisseur.setCmdeFournisseur(cmdSaved);
                ligneCmdeFournisseurRepository.save(ligneCmdeFournisseur);

                // entree en stock
                effectuerEntree(ligneCmdeFournisseur);
            });
        }
        return CmdeFournisseurDto.fromEntity(cmdSaved);
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
                        .map(CmdeFournisseurDto::fromEntity)
                        .orElseThrow(
                                ()-> new EntityNotFoundException(" cmdFournisseur not found with the ID {}")
                        );
    }

    @Override
    public CmdeFournisseurDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande fournisseur CODE is NULL");
            return cmdeFournisseurRepository.findByCode(code)
                    .map(CmdeFournisseurDto::fromEntity)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Aucune commande fournisseur n'a ete trouve avec le CODE " + code, ErrorCode.CMDE_FOURNISSEUR_NOT_FOUND
                    ));
        }
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
                        .map(CmdeFournisseurDto::fromEntity)
                        .toList();
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
        return ligneCmdeFournisseurRepository.findAllByCmdeFournisseurId(idCommande).stream()
                .map(LigneCmdeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CmdeFournisseurDto delete(Integer id) {
        if (id == null) {
            log.error("Commande fournisseur ID is NULL");
            return null;
        }

        CmdeFournisseur cmdeFournisseur =
                cmdeFournisseurRepository
                        .findById(id)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("cmdeFournisseur not found with the ID {}")
                        );

        List<LigneCmdeFournisseur> ligneCommandeFournisseurs = ligneCmdeFournisseurRepository.findAllByCmdeFournisseurId(id);
        if (!ligneCommandeFournisseurs.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer une commande fournisseur deja utilisee",
                    ErrorCode.CMDE_FOURNISSEUR_ALREADY_IN_USE);
        }

        cmdeFournisseurRepository.delete(cmdeFournisseur);

        return CmdeFournisseurDto.fromEntity(cmdeFournisseur);
    }



    @Override
    public CmdeFournisseurDto updateEtatCommande(Integer idCommande, EtatCmde etatCommande) {
        checkIdCommande(idCommande);
        if (!StringUtils.hasLength(String.valueOf(etatCommande))) {
            log.error("L'etat de la commande fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un etat null",
                    ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        CmdeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        commandeFournisseur.setEtatCommande(etatCommande);

        CmdeFournisseur savedCommande = cmdeFournisseurRepository.save(CmdeFournisseurDto.toEntity(commandeFournisseur));
        if (commandeFournisseur.isCommandeLivree()) {
            updateMvtStk(idCommande);
        }
        return CmdeFournisseurDto.fromEntity(savedCommande);
    }

    @Override
    public CmdeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CmdeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
       LigneCmdeFournisseur ligneCommandeFournisseur = ligneCmdeFournisseurRepository.findById(idLigneCommande).get();

        ligneCommandeFournisseur.setQuantite(quantite);
        ligneCmdeFournisseurRepository.save(ligneCommandeFournisseur);

        return commandeFournisseurDto;
    }

    @Override
    public CmdeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        checkIdCommande(idCommande);
        if (idFournisseur == null) {
            log.error("L'ID du fournisseur is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID fournisseur null",
                    ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        CmdeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
        Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
        if (fournisseurOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun fournisseur n'a ete trouve avec l'ID " + idFournisseur, ErrorCode.FOURNISSEUR_NOT_FOUND);
        }
        commandeFournisseur.setFournisseurDto(FournisseurDto.fromEntity(fournisseurOptional.get()));

        return CmdeFournisseurDto.fromEntity(
                cmdeFournisseurRepository.save(CmdeFournisseurDto.toEntity(commandeFournisseur))
        );
    }

    @Override
    public CmdeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle, "nouvel");

        CmdeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);

        Optional<LigneCmdeFournisseur> ligneCommandeFournisseur = ligneCmdeFournisseurRepository.findById(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(idArticle);
        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune article n'a ete trouve avec l'ID " + idArticle, ErrorCode.ARTICLE_NOT_FOUND);
        }

        objectValidatorArticleDto.validate(ArticleDto.fromEntity(articleOptional.get()));

        LigneCmdeFournisseur ligneCommandeFournisseurToSaved = ligneCommandeFournisseur.get();
        ligneCommandeFournisseurToSaved.setArticle(articleOptional.get());
        ligneCmdeFournisseurRepository.save(ligneCommandeFournisseurToSaved);

        return commandeFournisseurDto;
    }

    @Override
    public CmdeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CmdeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
        // Just to check the LigneCommandeFournisseur and inform the fournisseur in case it is absent
        ligneCmdeFournisseurRepository.deleteById(idLigneCommande);
        ligneCmdeFournisseurRepository.deleteById(idLigneCommande);

        return commandeFournisseurDto;
    }

    private CmdeFournisseurDto checkEtatCommande(Integer idCommande) {
        CmdeFournisseurDto commandeFournisseur = findById(idCommande);
        if (commandeFournisseur.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        return commandeFournisseur;
    }

    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            log.error("Commande fournisseur ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
                    ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdLigneCommande(Integer idLigneCommande) {
        if (idLigneCommande == null) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
                    ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID de " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
                    ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }
    }

    private void updateMvtStk(Integer idCommande) {
        List<LigneCmdeFournisseur> ligneCommandeFournisseur = ligneCmdeFournisseurRepository.findAllByCmdeFournisseurId(idCommande);
        ligneCommandeFournisseur.forEach(lig -> {
            effectuerEntree(lig);
        });
    }

    private void effectuerEntree(LigneCmdeFournisseur lig) {
        MvtStockDto mvtStkDto = MvtStockDto.builder()
                .articleDto(ArticleDto.fromEntity(lig.getArticle()))
                .dateMvt(Instant.now())
                .typeMvt(TypeMvtStock.ENTREE)
                .sourceMvt(SourceMvtStock.COMMANDE_FOURNISSEUR)
                .quantite(lig.getQuantite())
                .entrepriseId(lig.getIdEntreprise())
                .build();
        mvtStkService.entreeStock(mvtStkDto);
    }
}

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
import com.k48.stock_management_system.notificationConfig.EmailService;
import com.k48.stock_management_system.notificationConfig.EmailService;
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
    private final MvtStockService mvtStkService;
    private final ObjectValidator<ArticleDto> objectValidatorArticleDto;
    private final EmailService emailService;


    @Override
    public CmdeFournisseurDto save(CmdeFournisseurDto cmdFournisseurDto) {

        // validate the cmdeFournisseurDto
        objectValidator.validate(cmdFournisseurDto);
        
        // Vérifier l'unicité du code de commande
        if (cmdFournisseurDto.getCode() != null) {
            Optional<CmdeFournisseur> existingByCode = cmdeFournisseurRepository.findByCode(cmdFournisseurDto.getCode());
            if (existingByCode.isPresent()) {
                log.error("Une commande fournisseur avec le code {} existe déjà", cmdFournisseurDto.getCode());
                throw new InvalidOperationException(
                    "Une commande fournisseur avec ce code existe déjà", 
                    ErrorCode.CMDE_FOURNISSEUR_ALREADY_EXISTS
                );
            }
        }

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

        // Convertir le DTO en entité
        CmdeFournisseur cmdeFournisseur = CmdeFournisseurDto.toEntity(cmdFournisseurDto);
        
        // Associer le fournisseur existant (récupéré de la base)
        cmdeFournisseur.setFournisseur(supplier);
        
        // Définir la date de la commande
        cmdeFournisseur.setDateCmde(Instant.now());
        
        // Définir l'état initial selon la logique métier
        cmdeFournisseur.setEtatCommande(EtatCmde.EN_PREPARATION);

        // save the cmdeFournisseur
        CmdeFournisseur cmdSaved = cmdeFournisseurRepository.save(cmdeFournisseur);
        final CmdeFournisseur finalSavedCmdeFournisseur = cmdSaved;
        
        // Envoyer email au fournisseur
        envoyerEmailCommandeFournisseur(supplier, cmdSaved);
        
        // Envoyer email au fournisseur
        envoyerEmailCommandeFournisseur(supplier, cmdSaved);

        // save the LineCmdeFournisseur
        if(cmdFournisseurDto.getLigneCmdeFournisseurDtos() != null) {
            cmdFournisseurDto.getLigneCmdeFournisseurDtos().forEach(ligcmdf -> {
                LigneCmdeFournisseur ligneCmdeFournisseur = LigneCmdeFournisseurDto.toEntity(ligcmdf);
                ligneCmdeFournisseur.setCmdeFournisseur(finalSavedCmdeFournisseur);
                ligneCmdeFournisseurRepository.save(ligneCmdeFournisseur);

                // Pas d'entrée de stock lors de la création (EN_PREPARATION)
                // Les entrées se feront lors du passage à l'état VALIDEE
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
            return null;
        }
        return cmdeFournisseurRepository.findByCode(code)
                .map(CmdeFournisseurDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur n'a ete trouve avec le CODE " + code, ErrorCode.CMDE_FOURNISSEUR_NOT_FOUND
                ));
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
        if (idCommande == null) {
            log.error("ID commande is NULL");
            throw new InvalidOperationException("ID commande ne peut pas être null", ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }
        return ligneCmdeFournisseurRepository.findAllByCmdeFournisseurId(idCommande).stream()
                .map(LigneCmdeFournisseurDto::fromEntity)
                .toList();
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
        // verifier que l'ID de la commande n'est pas null
        checkIdCommande(idCommande);

        // verifier que l'etat de commande n'est pas null
        if (etatCommande == null) {
            log.error("L'état de la commande est NULL");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un état null",
                    ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CmdeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
        EtatCmde etatActuel = commandeFournisseurDto.getEtatCommande();
        
        // Valider la transition d'état
        if (!isTransitionValide(etatActuel, etatCommande)) {
            throw new InvalidOperationException(
                String.format("Transition invalide de %s vers %s", etatActuel, etatCommande),
                ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE
            );
        }
        
        // Appliquer la logique métier selon le nouvel état
        switch (etatCommande) {
            case VALIDEE:
                // Vérifier que la commande a des lignes (charger depuis la DB)
                List<LigneCmdeFournisseur> lignesCommande = ligneCmdeFournisseurRepository.findAllByCmdeFournisseurId(idCommande);
                if (lignesCommande.isEmpty()) {
                    throw new InvalidOperationException(
                        "Impossible de valider une commande sans articles",
                        ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE
                    );
                }
                // Effectuer les entrées de stock
                effectuerEntreesStock(idCommande);
                break;
                
            case LIVREE:
                // Vérifier que la commande était validée
                if (etatActuel != EtatCmde.VALIDEE) {
                    throw new InvalidOperationException(
                        "Seule une commande validée peut être livrée",
                        ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE
                    );
                }
                break;
        }

        // Récupérer l'entité existante et modifier seulement l'état
        CmdeFournisseur cmdeFournisseur = cmdeFournisseurRepository.findById(idCommande)
            .orElseThrow(() -> new EntityNotFoundException(
                "Commande fournisseur non trouvée avec l'ID " + idCommande,
                ErrorCode.CMDE_FOURNISSEUR_NOT_FOUND
            ));
        
        cmdeFournisseur.setEtatCommande(etatCommande);
        CmdeFournisseur savedCmdFournisseur = cmdeFournisseurRepository.save(cmdeFournisseur);
        
        // Envoyer notification selon l'état
        if (etatCommande == EtatCmde.VALIDEE) {
            envoyerEmailValidationFournisseur(savedCmdFournisseur.getFournisseur(), savedCmdFournisseur);
        }
        
        // Envoyer notification selon l'état
        if (etatCommande == EtatCmde.VALIDEE) {
            envoyerEmailValidationFournisseur(savedCmdFournisseur.getFournisseur(), savedCmdFournisseur);
        }

        return CmdeFournisseurDto.fromEntity(savedCmdFournisseur);
    }
    
    private boolean isTransitionValide(EtatCmde etatActuel, EtatCmde nouvelEtat) {
        // Si l'état actuel est null, on peut aller vers EN_PREPARATION ou VALIDEE
        if (etatActuel == null) {
            return nouvelEtat == EtatCmde.EN_PREPARATION || nouvelEtat == EtatCmde.VALIDEE;
        }
        
        return switch (etatActuel) {
            case EN_PREPARATION -> nouvelEtat == EtatCmde.VALIDEE;
            case VALIDEE -> nouvelEtat == EtatCmde.LIVREE;
            case LIVREE -> false; // Aucune transition possible depuis LIVREE
            default -> false;
        };
    }
    
    private void effectuerEntreesStock(Integer idCommande) {
        List<LigneCmdeFournisseur> lignes = ligneCmdeFournisseurRepository.findAllByCmdeFournisseurId(idCommande);
        lignes.forEach(this::effectuerEntree);
    }

    @Override
    public CmdeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("La quantité est invalide");
            throw new InvalidOperationException("Impossible de modifier avec une quantité null ou négative",
                    ErrorCode.CMDE_FOURNISSEUR_NON_MODIFIABLE);
        }

        CmdeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
        LigneCmdeFournisseur ligneCommandeFournisseur = ligneCmdeFournisseurRepository.findById(idLigneCommande)
            .orElseThrow(() -> new EntityNotFoundException(
                "Ligne de commande fournisseur non trouvée avec l'ID " + idLigneCommande,
                ErrorCode.LIGNE_CMDE_FOURNISSEUR_NOT_FOUND
            ));

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

        // Vérifier l'état de la commande et récupérer l'entité
        checkEtatCommande(idCommande);
        
        CmdeFournisseur cmdeFournisseur = cmdeFournisseurRepository.findById(idCommande)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande fournisseur trouvée avec l'ID " + idCommande,
                        ErrorCode.CMDE_FOURNISSEUR_NOT_FOUND
                ));

        // Charger le fournisseur
        Fournisseur fournisseur = fournisseurRepository.findById(idFournisseur)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun fournisseur trouvé avec l'ID " + idFournisseur,
                        ErrorCode.FOURNISSEUR_NOT_FOUND
                ));

        // Associer le fournisseur à la commande
        cmdeFournisseur.setFournisseur(fournisseur);

        // Sauvegarder la commande mise à jour
        CmdeFournisseur savedCmdeFournisseur = cmdeFournisseurRepository.save(cmdeFournisseur);

        return CmdeFournisseurDto.fromEntity(savedCmdeFournisseur);
    }

    @Override
    public CmdeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(idArticle, "nouvel");

        CmdeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);

        LigneCmdeFournisseur ligneCommandeFournisseur = ligneCmdeFournisseurRepository.findById(idLigneCommande)
            .orElseThrow(() -> new EntityNotFoundException(
                "Ligne de commande fournisseur non trouvée avec l'ID " + idLigneCommande,
                ErrorCode.LIGNE_CMDE_FOURNISSEUR_NOT_FOUND
            ));

        Article article = articleRepository.findById(idArticle)
            .orElseThrow(() -> new EntityNotFoundException(
                "Aucun article trouvé avec l'ID " + idArticle, 
                ErrorCode.ARTICLE_NOT_FOUND
            ));

        objectValidatorArticleDto.validate(ArticleDto.fromEntity(article));

        LigneCmdeFournisseur ligneCommandeFournisseurToSaved = ligneCommandeFournisseur;
        ligneCommandeFournisseurToSaved.setArticle(article);
        ligneCmdeFournisseurRepository.save(ligneCommandeFournisseurToSaved);

        return commandeFournisseurDto;
    }

    @Override
    public CmdeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CmdeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
        
        // Vérifier que la ligne de commande existe
        if (!ligneCmdeFournisseurRepository.existsById(idLigneCommande)) {
            throw new EntityNotFoundException(
                "Ligne de commande fournisseur non trouvée avec l'ID " + idLigneCommande,
                ErrorCode.LIGNE_CMDE_FOURNISSEUR_NOT_FOUND
            );
        }
        
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
    
    private void envoyerEmailCommandeFournisseur(Fournisseur fournisseur, CmdeFournisseur commande) {
        try {
            String subject = "Nouvelle commande #" + commande.getCode();
            String content = buildEmailCommandeContent(fournisseur, commande);
            emailService.sendConfirmationEmail(fournisseur.getEmail(), subject, content);
            log.info("Email de commande envoyé au fournisseur {} pour la commande {}", fournisseur.getEmail(), commande.getCode());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email au fournisseur {}: {}", fournisseur.getEmail(), e.getMessage());
        }
    }
    
    private void envoyerEmailValidationFournisseur(Fournisseur fournisseur, CmdeFournisseur commande) {
        try {
            String subject = "Commande #" + commande.getCode() + " validée";
            String content = "Bonjour,\n\n" +
                           "La commande #" + commande.getCode() + " a été validée.\n" +
                           "Merci de préparer la livraison.\n\n" +
                           "Cordialement,\nL'équipe";
            emailService.sendConfirmationEmail(fournisseur.getEmail(), subject, content);
            log.info("Email de validation envoyé au fournisseur {} pour la commande {}", fournisseur.getEmail(), commande.getCode());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de validation au fournisseur {}: {}", fournisseur.getEmail(), e.getMessage());
        }
    }
    
    private String buildEmailCommandeContent(Fournisseur fournisseur, CmdeFournisseur commande) {
        return "Bonjour,\n\n" +
               "Nous avons une nouvelle commande pour vous.\n" +
               "Numéro de commande: #" + commande.getCode() + "\n" +
               "Date de commande: " + commande.getDateCmde() + "\n\n" +
               "Merci de traiter cette commande dans les meilleurs délais.\n\n" +
               "Cordialement,\nL'équipe";
    }
}

package com.k48.stock_management_system.services.impl;

import com.k48.stock_management_system.dto.*;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.model.*;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.repositories.ClientRepository;
import com.k48.stock_management_system.repositories.CmdeClientRepository;
import com.k48.stock_management_system.repositories.LigneCmdeClientRepository;
import com.k48.stock_management_system.services.CmdeClientService;
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
public class CmdeClientServiceImpl implements CmdeClientService {

    private final CmdeClientRepository cmdeClientRepository;
    private final ClientRepository clientRepository;
    private final LigneCmdeClientRepository ligneCmdeClientRepository;
    private final ArticleRepository articleRepository;
    private final MvtStockService mvtStockService;
    private final ObjectValidator<CmdeClientDto> objectValidator;
    private final ObjectValidator<ArticleDto> articleValidator;



    @Override
    public CmdeClientDto save(CmdeClientDto cmdeClientDto) {

        //valider la commande
        objectValidator.validate(cmdeClientDto);
        
        // Vérifier l'unicité du code de commande
        if (cmdeClientDto.getCode() != null) {
            Optional<CmdeClient> existingByCode = cmdeClientRepository.findByCode(cmdeClientDto.getCode());
            if (existingByCode.isPresent()) {
                log.error("Une commande client avec le code {} existe déjà", cmdeClientDto.getCode());
                throw new InvalidOperationException(
                    "Une commande client avec ce code existe déjà", 
                    ErrorCode.CMDE_CLIENT_ALREADY_EXISTS
                );
            }
        }

        // verifier que le client  existe et le recuperer dans la BD
        Client client =
                clientRepository
                        .findById(cmdeClientDto.getClientDto().getId())
                        .orElseThrow(
                                ()-> new EntityNotFoundException("client with ID {}"+cmdeClientDto.getClientDto().getId()+" not found !")
                        );

        // verifier que l'article existe en BD pour chaque ligne de commande que l'on veut enregistrer
        if(cmdeClientDto.getLigneCmdeClientDtos() != null) {
            cmdeClientDto.getLigneCmdeClientDtos().forEach(ligCmdClt ->{
                if(ligCmdClt.getArticleId() != null) {
                    Article article =
                            articleRepository
                                    .findById(ligCmdClt.getArticleId())
                                    .orElseThrow(
                                            ()-> new EntityNotFoundException("article with ID {} "+ligCmdClt.getArticleId()+"not found in DB!")
                                    );

                }else{
                    log.warn("No Article found in DB we can't finalize the save operation");

                }
            });

        }

        // Convertir le DTO en entité
        CmdeClient cmdeClient = CmdeClientDto.toEntity(cmdeClientDto);

        // Associer le client existant (récupéré de la base)
        cmdeClient.setClient(client);

        // Définir la date de la commande
        cmdeClient.setDateCmde(Instant.now());
        
        // Définir l'état initial selon la logique métier
        cmdeClient.setEtatCommande(EtatCmde.EN_PREPARATION);

        // if everything is good save the cmdClient
        cmdeClient = cmdeClientRepository.save(cmdeClient);
        final CmdeClient finalSavedCmdeClient = cmdeClient;

        // save the LineCmdClient we can't persist the ligneCmdeClient without knowing  whose cmdClient it's belong to
        if(cmdeClientDto.getLigneCmdeClientDtos() != null) {

            cmdeClientDto.getLigneCmdeClientDtos().forEach(ligCmdClt ->{
                LigneCmdeClient ligneCmdeClient = LigneCmdeClientDto.toEntity(ligCmdClt);
                ligneCmdeClient.setCmdeClient(finalSavedCmdeClient);
                ligneCmdeClientRepository.save(ligneCmdeClient);
                
                // Pas de sortie de stock lors de la création (EN_PREPARATION)
                // Les sorties se feront lors du passage à l'état VALIDEE
            });
        }

        

        return CmdeClientDto.fromEntity(cmdeClient);
    }

    @Override
    public CmdeClientDto updateEtatCommande(Integer idCommande, EtatCmde etatCommande) {
        // verifier que l'ID de la commande n'est pas null
        checkIdCommande(idCommande);

        // verifier que l'etat de commande n'est pas null
        if (etatCommande == null) {
            log.error("L'état de la commande est NULL");
            throw new InvalidOperationException("Impossible de modifier l'état de la commande avec un état null",
                    ErrorCode.CMDE_CLIENT_NON_MODIFIABLE);
        }

        CmdeClientDto commandeClientDto = checkEtatCommande(idCommande);
        EtatCmde etatActuel = commandeClientDto.getEtatCommande();
        
        // Valider la transition d'état
        if (!isTransitionValide(etatActuel, etatCommande)) {
            throw new InvalidOperationException(
                String.format("Transition invalide de %s vers %s", etatActuel, etatCommande),
                ErrorCode.CMDE_CLIENT_NON_MODIFIABLE
            );
        }
        
        // Appliquer la logique métier selon le nouvel état
        switch (etatCommande) {
            case VALIDEE:
                // Vérifier que la commande a des lignes
                if (commandeClientDto.getLigneCmdeClientDtos() == null || 
                    commandeClientDto.getLigneCmdeClientDtos().isEmpty()) {
                    throw new InvalidOperationException(
                        "Impossible de valider une commande sans articles",
                        ErrorCode.CMDE_CLIENT_NON_MODIFIABLE
                    );
                }
                // Effectuer les sorties de stock
                effectuerSortiesStock(idCommande);
                break;
                
            case LIVREE:
                // Vérifier que la commande était validée
                if (etatActuel != EtatCmde.VALIDEE) {
                    throw new InvalidOperationException(
                        "Seule une commande validée peut être livrée",
                        ErrorCode.CMDE_CLIENT_NON_MODIFIABLE
                    );
                }
                break;
        }

        // Récupérer l'entité existante et modifier seulement l'état
        CmdeClient cmdeClient = cmdeClientRepository.findById(idCommande)
            .orElseThrow(() -> new EntityNotFoundException(
                "Commande client non trouvée avec l'ID " + idCommande,
                ErrorCode.CMDE_CLIENT_NOT_FOUND
            ));
        
        cmdeClient.setEtatCommande(etatCommande);
        CmdeClient savedCmdClt = cmdeClientRepository.save(cmdeClient);

        return CmdeClientDto.fromEntity(savedCmdClt);
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
        };
    }
    
    private void effectuerSortiesStock(Integer idCommande) {
        List<LigneCmdeClient> lignes = ligneCmdeClientRepository.findAllByCmdeClientId(idCommande);
        lignes.forEach(this::effectuerSortie);
    }

    @Override
    public CmdeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("La quantité est invalide");
            throw new InvalidOperationException("Impossible de modifier avec une quantité null ou négative",
                    ErrorCode.CMDE_CLIENT_NON_MODIFIABLE);
        }

        CmdeClientDto commandeClientDto = checkEtatCommande(idCommande);
        
        // Vérifier que la modification est autorisée selon l'état
        if (commandeClientDto.getEtatCommande() == EtatCmde.LIVREE) {
            throw new InvalidOperationException(
                "Impossible de modifier une commande livrée",
                ErrorCode.CMDE_CLIENT_NON_MODIFIABLE
            );
        }

        Optional<LigneCmdeClient> ligneCommandeClientOptional = ligneCmdeClientRepository.findById(idLigneCommande);
        if (ligneCommandeClientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                "Ligne de commande non trouvée avec l'ID " + idLigneCommande,
                ErrorCode.lIGNE_CMDE_CLIENT_NOT_FOUND
            );
        }

        LigneCmdeClient ligneCommandeClient = ligneCommandeClientOptional.get();
        ligneCommandeClient.setQuantite(quantite);
        ligneCmdeClientRepository.save(ligneCommandeClient);

        return commandeClientDto;
    }

    @Override
    public CmdeClientDto updateClient(Integer idCommande, Integer idClient) {

        checkIdCommande(idCommande);

        if (idClient == null) {
            log.error("L'ID du client is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID client null",
                    ErrorCode.CMDE_CLIENT_NON_MODIFIABLE);
        }

        // Charger la commande existante
        CmdeClient cmdeClient = cmdeClientRepository.findById(idCommande)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client trouvée avec l'ID " + idCommande,
                        ErrorCode.CMDE_CLIENT_NOT_FOUND
                ));

        // Vérifier l'état de la commande (si besoin)
        checkEtatCommande(idCommande);

        // Charger le client
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucun client trouvé avec l'ID " + idClient,
                        ErrorCode.CLIENT_NOT_FOUND
                ));


        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if (clientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun client n'a ete trouve avec l'ID " + idClient, ErrorCode.CLIENT_NOT_FOUND);
        }

        // Associer le client à la commande
        cmdeClient.setClient(client);

        // Sauvegarder la commande mise à jour
        CmdeClient savedCmdeClient = cmdeClientRepository.save(cmdeClient);


        return CmdeClientDto.fromEntity(savedCmdeClient);

    }

    @Override
    public CmdeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);
        checkIdArticle(newIdArticle, "nouvel");

        CmdeClientDto commandeClientDto = checkEtatCommande(idCommande);

        Optional<LigneCmdeClient> ligneCommandeClient = ligneCmdeClientRepository.findById(idLigneCommande);

        Optional<Article> articleOptional = articleRepository.findById(newIdArticle);
        if (articleOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucune article n'a ete trouve avec l'ID " + newIdArticle, ErrorCode.ARTICLE_NOT_FOUND);
        }

        articleValidator.validate(ArticleDto.fromEntity(articleOptional.get()));

        LigneCmdeClient ligneCommandeClientToSaved = ligneCommandeClient.get();
        ligneCommandeClientToSaved.setArticle(articleOptional.get());
        ligneCmdeClientRepository.save(ligneCommandeClientToSaved);

        return commandeClientDto;
    }

    @Override
    public CmdeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        CmdeClientDto commandeClientDto = checkEtatCommande(idCommande);
        
        // Vérifier que la modification est autorisée selon l'état
        if (commandeClientDto.getEtatCommande() == EtatCmde.LIVREE) {
            throw new InvalidOperationException(
                "Impossible de supprimer un article d'une commande livrée",
                ErrorCode.CMDE_CLIENT_NON_MODIFIABLE
            );
        }
        
        // Vérifier que la ligne de commande existe et appartient à cette commande
        LigneCmdeClient ligneCommande = ligneCmdeClientRepository.findById(idLigneCommande)
            .orElseThrow(() -> new EntityNotFoundException(
                "Ligne de commande non trouvée avec l'ID " + idLigneCommande,
                ErrorCode.lIGNE_CMDE_CLIENT_NOT_FOUND
            ));
            
        // Vérifier que la ligne appartient bien à cette commande
        if (!ligneCommande.getCmdeClient().getId().equals(idCommande)) {
            throw new InvalidOperationException(
                "La ligne de commande ne correspond pas à cette commande",
                ErrorCode.CMDE_CLIENT_NON_MODIFIABLE
            );
        }
        
        // Supprimer la ligne de commande
        ligneCmdeClientRepository.deleteById(idLigneCommande);
        log.info("Article supprimé de la commande {} (ligne {})", idCommande, idLigneCommande);

        return commandeClientDto;
    }



    @Override
    public CmdeClientDto findByCode(String code) {
        if (!StringUtils.hasLength(code)) {
            log.error("Commande client CODE is NULL");
            return null;
        }
        return cmdeClientRepository.findByCode(code)
                .map(CmdeClientDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Aucune commande client n'a ete trouve avec le CODE " + code, ErrorCode.CMDE_CLIENT_NOT_FOUND
                ));
    }

    @Override
    public List<CmdeClientDto> findAll() {
        List<CmdeClient>  cmdClients = cmdeClientRepository.findAll();
        return
                Optional.of(cmdClients)
                        .filter(listCmdClt -> !listCmdClt.isEmpty())
                        .orElseThrow(
                                ()->new EntityNotFoundException("EMPTY LIST {} no CMD_CLIENT"))
                        .stream()
                        .map(CmdeClientDto::fromEntity)
                        .toList();
    }

    @Override
    public List<LigneCmdeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
        return ligneCmdeClientRepository.findAllByCmdeClientId(idCommande).stream()
                .map(LigneCmdeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CmdeClientDto delete(Integer id) {
        if(id == null) {
            log.error("id is null");
            return null;
        }

        // Vérifier que la commande existe
        CmdeClient cmdeClient = cmdeClientRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "Commande client non trouvée avec l'ID " + id, 
                ErrorCode.CMDE_CLIENT_NOT_FOUND
            ));
        
        // Vérifier que la commande peut être supprimée (pas livrée)
        if (cmdeClient.getEtatCommande() == EtatCmde.LIVREE) {
            throw new InvalidOperationException(
                "Impossible de supprimer une commande livrée",
                ErrorCode.CMDE_CLIENT_NON_MODIFIABLE
            );
        }
        
        // Supprimer d'abord toutes les lignes de commande
        List<LigneCmdeClient> lignesCommande = ligneCmdeClientRepository.findAllByCmdeClientId(id);
        if (!lignesCommande.isEmpty()) {
            log.info("Suppression de {} lignes de commande pour la commande {}", lignesCommande.size(), id);
            ligneCmdeClientRepository.deleteAll(lignesCommande);
        }
        
        // Puis supprimer la commande
        cmdeClientRepository.delete(cmdeClient);
        log.info("Commande client {} supprimée avec succès", id);

        return CmdeClientDto.fromEntity(cmdeClient);
    }
    private void effectuerSortie(LigneCmdeClient lig) {
        MvtStockDto mvtStkDto = MvtStockDto.builder()
                .articleDto(ArticleDto.fromEntity(lig.getArticle()))
                .dateMvt(Instant.now())
                .typeMvt(TypeMvtStock.SORTIE)
                .sourceMvt(SourceMvtStock.COMMANDE_CLIENT)
                .quantite(lig.getQuantite())
                .entrepriseId(lig.getIdEntreprise())
                .build();
        mvtStockService.sortieStock(mvtStkDto);
    }

    private CmdeClientDto checkEtatCommande(Integer idCommande) {
        CmdeClientDto commandeClientDto = findById(idCommande);
        if (commandeClientDto.isCommandeLivree()) {
            throw new InvalidOperationException("Impossible de modifier la commande lorsqu'elle est livree", ErrorCode.CMDE_CLIENT_NON_MODIFIABLE);
        }
        return commandeClientDto;
    }

    @Override
    public CmdeClientDto findById(Integer cmdCltId) {
        if (cmdCltId == null) {
            log.error("Commande client ID is NULL");
            return null;
        }
        return
                cmdeClientRepository
                        .findById(cmdCltId)
                        .map(CmdeClientDto::fromEntity)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("cmdClt with ID {} "+cmdCltId+"not found ",  ErrorCode.CMDE_CLIENT_NOT_FOUND)
                        );
    }

    private void checkIdCommande(Integer idCommande) {
        if (idCommande == null) {
            log.error("Commande client ID is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un ID null",
                    ErrorCode.CMDE_CLIENT_NON_MODIFIABLE);
        }

    }

    private void checkIdLigneCommande(Integer idLigneCmde) {
        if (idLigneCmde == null) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une ligne de commande null",
                    ErrorCode.CMDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void checkIdArticle(Integer idArticle, String msg) {
        if (idArticle == null) {
            log.error("L'ID de " + msg + " is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec un " + msg + " ID article null",
                    ErrorCode.CMDE_CLIENT_NON_MODIFIABLE);
        }
    }

    private void updateMvtStk(Integer idCommande) {
        List<LigneCmdeClient> ligneCommandeClients = ligneCmdeClientRepository.findAllByCmdeClientId(idCommande);
        ligneCommandeClients.forEach(lig -> {
            effectuerSortie(lig);
        });
    }




}

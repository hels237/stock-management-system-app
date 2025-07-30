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

        // verifier que le client  existe dans la BD
        Client client =
                clientRepository
                        .findById(cmdeClientDto.getClientDto().getId())
                        .orElseThrow(
                                ()-> new EntityNotFoundException("client with ID {}"+cmdeClientDto.getClientDto().getId()+" not found !")
                        );

        // verifier que l'article existe en BD pour chaque ligne de commande que l'on veut enregistrer
        if(cmdeClientDto.getLigneCmdeClientDtos() != null) {
            cmdeClientDto.getLigneCmdeClientDtos().forEach(ligCmdClt ->{
                if(ligCmdClt.getArticleDto() != null) {
                    Article article =
                            articleRepository
                                    .findById(ligCmdClt.getArticleDto().getId())
                                    .orElseThrow(
                                            ()-> new EntityNotFoundException("article with ID {} "+ligCmdClt.getArticleDto().getId()+"not found in DB!")
                                    );

                }else{
                    log.warn("No Article found in DB we can't finalize the save operation");

                }
            });

        }


        // Définir la date de la commande
        cmdeClientDto.setDateCmde(Instant.now());

        // if everything is good save the cmdClient
        CmdeClient cmdeClient = cmdeClientRepository.save(CmdeClientDto.toEntity(cmdeClientDto));

        // save the LineCmdClient we can't persist the ligneCmdeClient without knowing  whose cmdClient it's belong to
        if(cmdeClientDto.getLigneCmdeClientDtos() != null) {

            cmdeClientDto.getLigneCmdeClientDtos().forEach(ligCmdClt ->{
                LigneCmdeClient ligneCmdeClient = LigneCmdeClientDto.toEntity(ligCmdClt);
                ligneCmdeClient.setCmdeClient( cmdeClient );
                ligneCmdeClientRepository.save(ligneCmdeClient);

                effectuerSortie(ligneCmdeClient);

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
        commandeClientDto.setEtatCommande(etatCommande);

        CmdeClient savedCmdClt = cmdeClientRepository.save(CmdeClientDto.toEntity(commandeClientDto));
        if (commandeClientDto.isCommandeLivree()) {
            updateMvtStk(idCommande);
        }

        return  CmdeClientDto.fromEntity(savedCmdClt);
    }

    @Override
    public CmdeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        checkIdCommande(idCommande);
        checkIdLigneCommande(idLigneCommande);

        if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
            log.error("L'ID de la ligne commande is NULL");
            throw new InvalidOperationException("Impossible de modifier l'etat de la commande avec une quantite null ou ZERO",
                    ErrorCode.CMDE_CLIENT_NON_MODIFIABLE);
        }

        CmdeClientDto commandeClientDto = checkEtatCommande(idCommande);
        Optional<LigneCmdeClient> ligneCommandeClientOptional = ligneCmdeClientRepository.findById(idLigneCommande);

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
        CmdeClientDto commandeClientDto = checkEtatCommande(idCommande);
        Optional<Client> clientOptional = clientRepository.findById(idClient);
        if (clientOptional.isEmpty()) {
            throw new EntityNotFoundException(
                    "Aucun client n'a ete trouve avec l'ID " + idClient, ErrorCode.CLIENT_NOT_FOUND);
        }
        commandeClientDto.setClientDto(ClientDto.fromEntity(clientOptional.get()));

        return CmdeClientDto.fromEntity(
                cmdeClientRepository.save(CmdeClientDto.toEntity(commandeClientDto))
        );


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

        CmdeClientDto commandeClientDto= checkEtatCommande(idCommande);
        // Just to check the LigneCommandeClient and inform the client in case it is absent
        ligneCmdeClientRepository.findById(idLigneCommande);
        ligneCmdeClientRepository.deleteById(idLigneCommande);

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

        //le orElseThrow()  par defaut leve une eexception NoSuchElementFound
        CmdeClient cmdeClient = cmdeClientRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("cmdClient not found with ID " + id));
        cmdeClientRepository.delete(cmdeClient);

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

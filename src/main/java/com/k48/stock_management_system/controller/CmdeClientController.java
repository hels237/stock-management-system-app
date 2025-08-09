package com.k48.stock_management_system.controller;


import com.k48.stock_management_system.controller.api.CmdClientApi;
import com.k48.stock_management_system.dto.CmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.model.EtatCmde;
import com.k48.stock_management_system.services.CmdeClientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@Tag(name = "Commande Client", description = "API pour la gestion des commandes clients")
@RestController
@RequestMapping(APP_ROOT + "cmdeclient")
@RequiredArgsConstructor
public class CmdeClientController implements CmdClientApi {

    private final CmdeClientService cmdeClientService;


    @Override
    public CmdeClientDto save(CmdeClientDto dto) {
        return cmdeClientService.save(dto);
    }

    @Override
    public CmdeClientDto updateEtatCmde(Integer idCommande, EtatCmde etatCommande) {
        return cmdeClientService.updateEtatCommande(idCommande, etatCommande);
    }

    @Override
    public CmdeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        return cmdeClientService.updateQuantiteCommande(idCommande, idLigneCommande, quantite);
    }

    @Override
    public CmdeClientDto updateClient(Integer idCommande, Integer idClient) {
        return cmdeClientService.updateClient(idCommande, idClient);
    }

    @Override
    public CmdeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        return cmdeClientService.updateArticle(idCommande, idLigneCommande, idArticle);
    }

    @Override
    public CmdeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        return cmdeClientService.deleteArticle(idCommande, idLigneCommande);
    }

    @Override
    public CmdeClientDto findById(Integer idCommandeClient) {
        return cmdeClientService.findById(idCommandeClient);
    }

    @Override
    public CmdeClientDto findByCode(String code) {
        return cmdeClientService.findByCode(code);
    }

    @Override
    public List<CmdeClientDto> findAll() {
        return cmdeClientService.findAll();
    }

    @Override
    public List<LigneCmdeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
        return cmdeClientService.findAllLignesCommandesClientByCommandeClientId(idCommande);
    }

    @Override
    public CmdeClientDto delete(Integer id) {
        return cmdeClientService.delete(id);
    }


}

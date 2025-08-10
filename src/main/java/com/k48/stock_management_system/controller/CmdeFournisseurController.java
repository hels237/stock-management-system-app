package com.k48.stock_management_system.controller;


import com.k48.stock_management_system.controller.api.CmdeFournisseurApi;
import com.k48.stock_management_system.dto.CmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.model.EtatCmde;
import com.k48.stock_management_system.services.CmdeFournisseurService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;

@Tag(name = "Commande Fournisseur", description = "API pour la gestion des commandes fournisseurs")
@RestController
@RequestMapping(APP_ROOT + "commandefournisseur")
@RequiredArgsConstructor
public class CmdeFournisseurController implements CmdeFournisseurApi {

    private final CmdeFournisseurService cmdeFournisseurService;

    @Override
    public CmdeFournisseurDto save(CmdeFournisseurDto dto) {
        return cmdeFournisseurService.save(dto);
    }

    @Override
    public CmdeFournisseurDto updateEtatCommande(Integer idCommande, EtatCmde etatCommande) {
        return cmdeFournisseurService.updateEtatCommande(idCommande, etatCommande);
    }

    @Override
    public CmdeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
        return cmdeFournisseurService.updateQuantiteCommande(idCommande, idLigneCommande, quantite);
    }

    @Override
    public CmdeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
        return cmdeFournisseurService.updateFournisseur(idCommande, idFournisseur);
    }

    @Override
    public CmdeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
        return cmdeFournisseurService.updateArticle(idCommande, idLigneCommande, idArticle);
    }

    @Override
    public CmdeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
        return cmdeFournisseurService.deleteArticle(idCommande, idLigneCommande);
    }

    @Override
    public CmdeFournisseurDto findById(Integer id) {
        return cmdeFournisseurService.findById(id);
    }

    @Override
    public CmdeFournisseurDto findByCode(String code) {
        return cmdeFournisseurService.findByCode(code);
    }

    @Override
    public List<CmdeFournisseurDto> findAll() {
        return cmdeFournisseurService.findAll();
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
        return cmdeFournisseurService.findAllLignesCommandesFournisseurByCommandeFournisseurId(idCommande);
    }

    @Override
    public CmdeFournisseurDto delete(Integer id) {
        return cmdeFournisseurService.delete(id);
    }
}

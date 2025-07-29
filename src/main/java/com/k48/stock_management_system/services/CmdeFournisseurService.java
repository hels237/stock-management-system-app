package com.k48.stock_management_system.services;


import com.k48.stock_management_system.dto.CmdeClientDto;
import com.k48.stock_management_system.dto.CmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.model.EtatCmde;

import java.math.BigDecimal;
import java.util.List;

public interface CmdeFournisseurService extends AbstractService<CmdeFournisseurDto> {

    CmdeFournisseurDto save(CmdeFournisseurDto dto);

    CmdeFournisseurDto updateEtatCommande(Integer idCommande, EtatCmde etatCommande);

    CmdeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CmdeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur);

    CmdeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle);

    // Delete article ==> delete LigneCommandeFournisseur
    CmdeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande);

    CmdeFournisseurDto findById(Integer id);

    CmdeFournisseurDto findByCode(String code);

    List<CmdeFournisseurDto> findAll();

    List<LigneCmdeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande);

    CmdeFournisseurDto delete(Integer id);
}

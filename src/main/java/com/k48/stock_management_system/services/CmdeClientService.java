package com.k48.stock_management_system.services;


import com.k48.stock_management_system.dto.CmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.model.EtatCmde;

import java.math.BigDecimal;
import java.util.List;

public interface CmdeClientService extends AbstractService<CmdeClientDto> {
    CmdeClientDto save(CmdeClientDto dto);

    CmdeClientDto updateEtatCommande(Integer idCommande, EtatCmde etatCommande);

    CmdeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);

    CmdeClientDto updateClient(Integer idCommande, Integer idClient);

    CmdeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer newIdArticle);

    // Delete article ==> delete LigneCommandeClient
    CmdeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande);

    CmdeClientDto findById(Integer id);

    CmdeClientDto findByCode(String code);

    List<CmdeClientDto> findAll();

    List<LigneCmdeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande);

    CmdeClientDto delete(Integer id);
}

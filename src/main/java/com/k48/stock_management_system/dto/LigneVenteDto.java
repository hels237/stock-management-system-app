package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.model.LigneVente;
import com.k48.stock_management_system.model.Vente;
import lombok.*;

import java.math.BigDecimal;


@Builder
@Getter @Setter
public class LigneVenteDto {

    private BigDecimal prixUnitaire;

    private BigDecimal quantite;

    private Vente vente;

    private Article article;

    private Integer entrepriseId;


    public static LigneVenteDto toDto(LigneVente ligneVente){
        if(ligneVente == null){
            return null;
        }
        return LigneVenteDto
                .builder()
                .prixUnitaire(ligneVente.getPrixUnitaire())
                .quantite(ligneVente.getQuantite())
                .entrepriseId(ligneVente.getIdEntreprise())
                .build();
    }

    public static LigneVente toEntity(LigneVenteDto ligneVenteDto){

        if(ligneVenteDto == null){
            return null;
        }
        return LigneVente
                .builder()
                .prixUnitaire(ligneVenteDto.getPrixUnitaire())
                .quantite(ligneVenteDto.getQuantite())
                .idEntreprise(ligneVenteDto.getEntrepriseId())
                .build();
    }


}

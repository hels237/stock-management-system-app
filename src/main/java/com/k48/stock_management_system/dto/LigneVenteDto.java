package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.model.LigneVente;
import com.k48.stock_management_system.model.Vente;
import lombok.*;

import java.math.BigDecimal;


@Builder
@Getter @Setter
public class LigneVenteDto {

    private Integer id;

    private BigDecimal prixUnitaire;

    private BigDecimal quantite;

    private VenteDto venteDto;

    private ArticleDto articleDto;

    private Integer entrepriseId;


    public static LigneVenteDto fromEntity(LigneVente ligneVente){
        if(ligneVente == null){
            return null;
        }
        return LigneVenteDto
                .builder()
                .id(ligneVente.getId())
                .articleDto(ArticleDto.fromEntity(ligneVente.getArticle()))
                .venteDto(VenteDto.fromEntity(ligneVente.getVente()))
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
                .article(ArticleDto.toEntity(ligneVenteDto.getArticleDto()))
                .vente(VenteDto.toEntity(ligneVenteDto.getVenteDto()))
                .prixUnitaire(ligneVenteDto.getPrixUnitaire())
                .quantite(ligneVenteDto.getQuantite())
                .idEntreprise(ligneVenteDto.getEntrepriseId())
                .build();
    }


}

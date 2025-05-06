package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.model.LigneCmdeClient;
import com.k48.stock_management_system.model.LigneCmdeFournisseur;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Builder
public class LigneCmdeFournisseurDto {

    private BigDecimal prixUnitaire;

    private BigDecimal quantite;

    private Integer cmdeFournisseurId;

    private Article articleId;




    public static LigneCmdeFournisseurDto toDto(LigneCmdeFournisseur ligneCmdeFournisseur) {

        if(ligneCmdeFournisseur == null) {
            return null;
        }
        return LigneCmdeFournisseurDto
                .builder()
                .prixUnitaire(ligneCmdeFournisseur.getPrixUnitaire())
                .quantite(ligneCmdeFournisseur.getQuantite())
                .build();
    }

    public static LigneCmdeFournisseur toEntity(LigneCmdeFournisseurDto ligneCmdeFournisseurDto) {

        if(ligneCmdeFournisseurDto == null) {
            return null;
        }
        return LigneCmdeFournisseur
                .builder()
                .prixUnitaire(ligneCmdeFournisseurDto.getPrixUnitaire())
                .quantite(ligneCmdeFournisseurDto.getQuantite())
                .build();
    }
}

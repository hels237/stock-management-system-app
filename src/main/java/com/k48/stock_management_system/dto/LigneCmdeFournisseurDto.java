package com.k48.stock_management_system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.k48.stock_management_system.model.LigneCmdeFournisseur;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Builder
public class LigneCmdeFournisseurDto {

    private Integer id;

    private BigDecimal prixUnitaire;

    private BigDecimal quantite;

    @JsonIgnore
    private CmdeFournisseurDto cmdeFournisseurDto;

    private ArticleDto articleDto;

    private Integer entrepriseId;




    public static LigneCmdeFournisseurDto fromEntity(LigneCmdeFournisseur ligneCmdeFournisseur) {

        if(ligneCmdeFournisseur == null) {
            return null;
        }
        return LigneCmdeFournisseurDto
                .builder()
                .id(ligneCmdeFournisseur.getId())
                .prixUnitaire(ligneCmdeFournisseur.getPrixUnitaire())
                .quantite(ligneCmdeFournisseur.getQuantite())
                .articleDto(ArticleDto.fromEntity(ligneCmdeFournisseur.getArticle()))
                .entrepriseId(ligneCmdeFournisseur.getIdEntreprise())
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
                .article(ArticleDto.toEntity(ligneCmdeFournisseurDto.getArticleDto()))
                .idEntreprise(ligneCmdeFournisseurDto.getEntrepriseId())
                .build();
    }
}

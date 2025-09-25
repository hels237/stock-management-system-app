package com.k48.stock_management_system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.k48.stock_management_system.model.CmdeClient;
import com.k48.stock_management_system.model.LigneCmdeClient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter @Getter
@Builder
public class LigneCmdeClientDto {

    private Integer id;

    private BigDecimal prixUnitaire;

    private BigDecimal quantite;

    @JsonIgnore
    private CmdeClient cmdeClient;

    private Integer articleId;

    private Integer entrepriseId;


    public static LigneCmdeClientDto fromEntity(LigneCmdeClient ligneCmdeClient) {

        if(ligneCmdeClient == null) {
            return null;
        }
        return LigneCmdeClientDto
                .builder()
                .id(ligneCmdeClient.getId())
                .prixUnitaire(ligneCmdeClient.getPrixUnitaire())
                .quantite(ligneCmdeClient.getQuantite())
                .articleId(ligneCmdeClient.getArticle() != null ? ligneCmdeClient.getArticle().getId() : null)
                .entrepriseId(ligneCmdeClient.getIdEntreprise())
                .build();
    }

    public static LigneCmdeClient toEntity(LigneCmdeClientDto ligneCmdeClientDto) {

        if(ligneCmdeClientDto == null) {
            return null;
        }
        return LigneCmdeClient
                .builder()
                .id(ligneCmdeClientDto.getId())
                .prixUnitaire(ligneCmdeClientDto.getPrixUnitaire())
                .quantite(ligneCmdeClientDto.getQuantite())
                .article(ligneCmdeClientDto.getArticleId() != null ? 
                    com.k48.stock_management_system.model.Article.builder().id(ligneCmdeClientDto.getArticleId()).build() : null)
                .idEntreprise(ligneCmdeClientDto.getEntrepriseId())
                .build();
    }
}

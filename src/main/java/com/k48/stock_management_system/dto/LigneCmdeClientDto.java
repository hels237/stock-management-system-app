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

    private ArticleDto articleDto;

    private Integer entrepriseId;


    public static LigneCmdeClientDto fromEntity(LigneCmdeClient ligneCmdeClient) {

        if(ligneCmdeClient == null) {
            return null;
        }
        return LigneCmdeClientDto

                .builder()
                .prixUnitaire(ligneCmdeClient.getPrixUnitaire())
                .quantite(ligneCmdeClient.getQuantite())
                .entrepriseId(ligneCmdeClient.getIdEntreprise())
                .build();
    }

    public static LigneCmdeClient toEntity(LigneCmdeClientDto ligneCmdeClientDto) {

        if(ligneCmdeClientDto == null) {
            return null;
        }
        return LigneCmdeClient
                .builder()
                .prixUnitaire(ligneCmdeClientDto.getPrixUnitaire())
                .quantite(ligneCmdeClientDto.getQuantite())
                .idEntreprise(ligneCmdeClientDto.getEntrepriseId())
                .build();
    }
}

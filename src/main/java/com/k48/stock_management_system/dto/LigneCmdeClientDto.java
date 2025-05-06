package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.CmdeClient;
import com.k48.stock_management_system.model.LigneCmdeClient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter @Getter
@Builder
public class LigneCmdeClientDto {

    private BigDecimal prixUnitaire;

    private BigDecimal quantite;

    private CmdeClient cmdeClient;

    private ArticleDto articleDto;


    public static LigneCmdeClientDto toDto(LigneCmdeClient ligneCmdeClient) {

        if(ligneCmdeClient == null) {
            return null;
        }
        return LigneCmdeClientDto
                .builder()
                .prixUnitaire(ligneCmdeClient.getPrixUnitaire())
                .quantite(ligneCmdeClient.getQuantite())
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
                .build();
    }
}

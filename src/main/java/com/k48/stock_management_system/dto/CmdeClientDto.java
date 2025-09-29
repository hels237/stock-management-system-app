package com.k48.stock_management_system.dto;


import com.k48.stock_management_system.model.CmdeClient;
import com.k48.stock_management_system.model.EtatCmde;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter @Getter
@Builder
public class CmdeClientDto {

    private Integer id;

    @NotNull
    private String code;

    private Instant dateCmde;

    private ClientDto clientDto;

    private EtatCmde etatCommande;

    private Integer entrepriseId;

    private List<LigneCmdeClientDto> ligneCmdeClientDtos;


    public static CmdeClientDto fromEntity(CmdeClient cmdeClient) {

        if (cmdeClient == null) {
            return null;
        }
        return CmdeClientDto
                .builder()
                .id(cmdeClient.getId())
                .clientDto(ClientDto.fromEntity(cmdeClient.getClient()))
                .code(cmdeClient.getCode())
                .dateCmde(cmdeClient.getDateCmde())
                .etatCommande(cmdeClient.getEtatCommande())
                .entrepriseId(cmdeClient.getIdEntreprise())
                .ligneCmdeClientDtos(cmdeClient.getLigneCmdeClients().stream().map(LigneCmdeClientDto::fromEntity).toList())
                .build();
    }

    public static CmdeClient toEntity(CmdeClientDto cmdeClientDto) {

        if (cmdeClientDto == null) {
            return null;
        }
        return CmdeClient.builder()
                .id(cmdeClientDto.getId())
                .client(ClientDto.toEntity(cmdeClientDto.getClientDto()))
                .code(cmdeClientDto.getCode())
                .dateCmde(cmdeClientDto.getDateCmde())
                .etatCommande(cmdeClientDto.getEtatCommande())
                .idEntreprise(cmdeClientDto.getEntrepriseId())
                .build();
    }

    public boolean isCommandeLivree() {
        return EtatCmde.LIVREE.equals(this.etatCommande);
    }

}

package com.k48.stock_management_system.dto;


import com.k48.stock_management_system.model.CmdeClient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter @Getter
@Builder
public class CmdeClientDto {

    private Integer id;

    private String code;

    private Instant dateCmde;

    private ClientDto clientDto;

    private Integer entrepriseId;

    private List<LigneCmdeClientDto> ligneCmdeClientDtos;


    public static CmdeClientDto fromEntity(CmdeClient cmdeClient) {

        if (cmdeClient == null) {
            return null;
        }
        return CmdeClientDto
                .builder()
                .clientDto(ClientDto.fromEntity(cmdeClient.getClient()))
                .code(cmdeClient.getCode())
                .dateCmde(cmdeClient.getDateCmde())
                .entrepriseId(cmdeClient.getIdEntreprise())
                .build();
    }

    public static CmdeClient toEntity(CmdeClientDto cmdeClientDto) {

        if (cmdeClientDto == null) {
            return null;
        }
        return CmdeClient.builder()
                .client(ClientDto.toEntity(cmdeClientDto.getClientDto()))
                .code(cmdeClientDto.getCode())
                .dateCmde(cmdeClientDto.getDateCmde())
                .idEntreprise(cmdeClientDto.getEntrepriseId())
                .build();
    }

}

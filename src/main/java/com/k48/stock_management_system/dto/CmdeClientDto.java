package com.k48.stock_management_system.dto;


import com.k48.stock_management_system.model.Client;
import com.k48.stock_management_system.model.CmdeClient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Setter @Getter
@Builder
public class CmdeClientDto {

    private String code;

    private Instant dateCmde;

    private ClientDto clientDto;

    private List<LigneCmdeClientDto> ligneCmdeClientDtos;


    public static CmdeClient toEntity(CmdeClientDto cmdeClientDto) {

        if (cmdeClientDto == null) {
            return null;
        }
        return CmdeClient
                .builder()
                .client(ClientDto.toEntity(cmdeClientDto.getClientDto()))
                .code(cmdeClientDto.getCode())
                .dateCmde(cmdeClientDto.getDateCmde())
                .build();
    }

    public static CmdeClientDto toDto(CmdeClient cmdeClient) {

        if (cmdeClient == null) {
            return null;
        }
        return CmdeClientDto.builder()
                .clientDto(ClientDto.toDto(cmdeClient.getClient()))
                .code(cmdeClient.getCode())
                .dateCmde(cmdeClient.getDateCmde())
                .build();
    }

}

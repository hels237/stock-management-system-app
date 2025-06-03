package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Setter
@Getter
@SuperBuilder
public class ClientDto {

    private Integer id;

    private String nom;

    private String prenom;

    private String email;

    private String photo;

    private Integer entrepriseId;

    private String numTelephone;

    private AdresseDto adresseDto;

    private List<CmdeClientDto> cmdeClientDtos;


    public static Client toEntity(ClientDto clientDto) {

        if(clientDto== null){
            return null;
        }
        return Client
                .builder()
                .adresse(AdresseDto.toEntity(clientDto.adresseDto))
                .nom(clientDto.getNom())
                .prenom(clientDto.getPrenom())
                .email(clientDto.getEmail())
                .numTelephone(clientDto.getNumTelephone())
                .build();
    }

    public static ClientDto toDto(Client client) {

        if(client == null){
            return null;
        }
        return ClientDto.builder()
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .adresseDto(AdresseDto.toDto(client.getAdresse()))
                .numTelephone(client.getNumTelephone())
                .build();
    }
}

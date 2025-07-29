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


    public static ClientDto fromEntity(Client client) {

        if(client == null){
            return null;
        }
        return ClientDto
                .builder()
                .adresseDto(AdresseDto.fromEntity(client.getAdresse()))
                .nom(client.getNom())
                .prenom(client.getPrenom())
                .email(client.getEmail())
                .numTelephone(client.getNumTelephone())
                .entrepriseId(client.getIdEntreprise())
                .build();
    }

    public static Client toEntity(ClientDto clientDto) {

        if(clientDto == null){
            return null;
        }
        return Client.builder()
                .nom(clientDto.getNom())
                .prenom(clientDto.getPrenom())
                .email(clientDto.getEmail())
                .adresse(AdresseDto.toEntity(clientDto.getAdresseDto()))
                .numTelephone(clientDto.getNumTelephone())
                .idEntreprise(clientDto.getEntrepriseId())
                .build();
    }
}

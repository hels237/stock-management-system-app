package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Entreprise;
import com.k48.stock_management_system.model.Fournisseur;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class FournisseurDto {

    private String nom;

    private String prenom;

    private String email;

    private String photo;

    private String numTelephone;

    private List<CmdeFournisseurDto> CmdeFournisseurDtos;


    public static FournisseurDto toDto(Fournisseur fournisseur) {

        if(fournisseur == null){
            return null;
        }
        return FournisseurDto.
                builder()
                .nom(fournisseur.getNom())
                .email(fournisseur.getEmail())
                .prenom(fournisseur.getPrenom())
                .build();
    }

    public static FournisseurDto toEntity(Fournisseur fournisseur) {

        if(fournisseur == null){
            return null;
        }
        return FournisseurDto.
                builder()
                .nom(fournisseur.getNom())
                .prenom(fournisseur.getPrenom())
                .email(fournisseur.getEmail())
                .build();
    }
}

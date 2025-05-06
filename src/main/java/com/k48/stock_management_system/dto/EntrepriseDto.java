package com.k48.stock_management_system.dto;


import com.k48.stock_management_system.model.CmdeFournisseur;
import com.k48.stock_management_system.model.Entreprise;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class EntrepriseDto {

    private String nom;

    private String description;

    private AdresseDto adresseDto;

    private String codeFiscal;

    private String photo;

    private String email;

    private String numTelephone;

    private String siteWeb;

    private List<UtilisateurDto> utilisateurDtos;


    private List<ArticleDto> articleDtos;


    public static EntrepriseDto toDto(Entreprise entreprise) {

        if(entreprise == null){
            return null;
        }
        return EntrepriseDto.
                builder()
                .codeFiscal(entreprise.getCodeFiscal())
                .nom(entreprise.getNom())
                .email(entreprise.getEmail())
                .build();
    }

    public static Entreprise toEntity(EntrepriseDto entrepriseDto) {

        if(entrepriseDto == null){
            return null;
        }
        return Entreprise.
                builder()
                .nom(entrepriseDto.getNom())
                .email(entrepriseDto.getEmail())
                .codeFiscal(entrepriseDto.getCodeFiscal())
                .build();
    }
}

package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Fournisseur;
import com.k48.stock_management_system.model.Utilisateur;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Builder
public class UtilisateurDto {

    private String nom;

    private String prenom;

    private String email;

    private String photo;

    //private String motDePasse;

    private EntrepriseDto entrepriseDto;



    public static UtilisateurDto toDto(Utilisateur utilisateur) {

        if(utilisateur == null){
            return null;
        }
        return UtilisateurDto.
                builder()
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .prenom(utilisateur.getPrenom())
                .build();
    }

    public static Utilisateur toEntity(UtilisateurDto utilisateurDto) {

        if(utilisateurDto == null){
            return null;
        }
        return Utilisateur.
                builder()
                .nom(utilisateurDto.getNom())
                .prenom(utilisateurDto.getPrenom())
                .email(utilisateurDto.getEmail())
                .build();
    }

}

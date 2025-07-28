package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Utilisateur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Builder
public class UtilisateurDto {

    @NotNull(message = "please fill the field")
    private String nom;

    @NotNull
    private String prenom;

    @Email
    private String email;

    private String photo;

    @NotNull
    private String motDePasse;

    @NotNull
    private AdresseDto adresseDto;

    private EntrepriseDto entrepriseDto;

    private Integer entrepriseId;



    public static UtilisateurDto toDto(Utilisateur utilisateur) {

        if(utilisateur == null){
            return null;
        }
        return UtilisateurDto.
                builder()
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .prenom(utilisateur.getPrenom())
                .adresseDto(AdresseDto.toDto(utilisateur.getAdresse()))
                .entrepriseId(utilisateur.getIdEntreprise())
                .entrepriseDto(EntrepriseDto.toDto(utilisateur.getEntreprise()))
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
                .adresse(AdresseDto.toEntity(utilisateurDto.getAdresseDto()))
                .idEntreprise(utilisateurDto.getEntrepriseId())
                .entreprise(EntrepriseDto.toEntity(utilisateurDto.entrepriseDto))
                .build();
    }

}

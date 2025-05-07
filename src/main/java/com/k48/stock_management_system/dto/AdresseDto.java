package com.k48.stock_management_system.dto;


import com.k48.stock_management_system.model.Adresse;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class AdresseDto {

    @NotNull
    private String adresse1;

    private String adresse2;

    @NotNull
    private String ville;

    @NotNull
    private String pays;

    private String codePostal;


    public static Adresse toEntity(AdresseDto adresseDto) {

        if(adresseDto== null){
            return null;
        }
        return Adresse
                .builder()
                .adresse1(adresseDto.getAdresse1())
                .adresse2(adresseDto.getAdresse2())
                .pays(adresseDto.getPays())
                .ville(adresseDto.getVille())
                .codePostal(adresseDto.getCodePostal())
                .build();
    }

    public static AdresseDto toDto(Adresse adresse) {

        if(adresse == null){
            return null;
        }
        return AdresseDto.
                builder()
                .adresse1(adresse.getAdresse1())
                .adresse2(adresse.getAdresse2())
                .pays(adresse.getPays())
                .ville(adresse.getVille())
                .codePostal(adresse.getCodePostal())
                .build();
    }

}

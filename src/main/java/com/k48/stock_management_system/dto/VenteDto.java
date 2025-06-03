package com.k48.stock_management_system.dto;


import com.k48.stock_management_system.model.LigneVente;
import com.k48.stock_management_system.model.Vente;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class VenteDto {


    private String code;

    private String commentaire;

    private Instant dateVente;

    private List<LigneVente> ligneVentes;

    private Integer entrepriseId;


    public static VenteDto toDto(Vente vente) {
        if(vente == null) {
            return null;
        }

        return
                VenteDto
                        .builder()
                        .code(vente.getCode())
                        .commentaire(vente.getCommentaire())
                        .dateVente(vente.getDateVente())
                        .build();
    }

    public static Vente toEntity(VenteDto venteDto) {
        if(venteDto == null) {
            return null;
        }

        return
                Vente
                        .builder()
                        .code(venteDto.getCode())
                        .commentaire(venteDto.getCommentaire())
                        .dateVente(venteDto.getDateVente())
                        .build();
    }


}

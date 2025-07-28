package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.CmdeFournisseur;
import com.k48.stock_management_system.model.LigneCmdeFournisseur;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter @Setter
@Builder
public class CmdeFournisseurDto {

    private String code;

    private Instant dateCmde;

    private FournisseurDto fournisseurDto;

    private List<LigneCmdeFournisseurDto> ligneCmdeFournisseurDtos;

    private Integer entrepriseId;


    public static CmdeFournisseurDto toDto(CmdeFournisseur cmdeFournisseur) {

        if(cmdeFournisseur== null){
            return null;
        }

        return CmdeFournisseurDto.
                builder()
                .code(cmdeFournisseur.getCode())
                .dateCmde(cmdeFournisseur.getDateCmde())
                .entrepriseId(cmdeFournisseur.getIdEntreprise())
                .build();
    }

    public static CmdeFournisseur toEntity(CmdeFournisseurDto cmdeFournisseurDto) {

        if(cmdeFournisseurDto == null){
            return null;
        }
        return CmdeFournisseur.
                builder()
                .code(cmdeFournisseurDto.getCode())
                .dateCmde(cmdeFournisseurDto.getDateCmde())
                .idEntreprise(cmdeFournisseurDto.getEntrepriseId())
                .build();
    }

}

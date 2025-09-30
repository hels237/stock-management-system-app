package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.CmdeFournisseur;
import com.k48.stock_management_system.model.EtatCmde;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter @Setter
@Builder
public class CmdeFournisseurDto {

    private Integer id;

    private String code;

    private Instant dateCmde;

    private EtatCmde etatCommande;

    private FournisseurDto fournisseurDto;

    private List<LigneCmdeFournisseurDto> ligneCmdeFournisseurDtos;

    private Integer entrepriseId;


    public static CmdeFournisseurDto fromEntity(CmdeFournisseur cmdeFournisseur) {

        if(cmdeFournisseur== null){
            return null;
        }

        return CmdeFournisseurDto.
                builder()
                .id(cmdeFournisseur.getId())
                .code(cmdeFournisseur.getCode())
                .dateCmde(cmdeFournisseur.getDateCmde())
                .entrepriseId(cmdeFournisseur.getIdEntreprise())
                .etatCommande(cmdeFournisseur.getEtatCommande())
                .ligneCmdeFournisseurDtos(cmdeFournisseur.getLigneCmdeFournisseur().stream().map(LigneCmdeFournisseurDto::fromEntity).toList())
                .fournisseurDto(FournisseurDto.fromEntity(cmdeFournisseur.getFournisseur()))
                .build();
    }

    public static CmdeFournisseur toEntity(CmdeFournisseurDto cmdeFournisseurDto) {

        if(cmdeFournisseurDto == null){
            return null;
        }
        return CmdeFournisseur.
                builder()
                .code(cmdeFournisseurDto.getCode())
                .etatCommande(cmdeFournisseurDto.getEtatCommande())
                .ligneCmdeFournisseur(cmdeFournisseurDto.getLigneCmdeFournisseurDtos().stream().map(LigneCmdeFournisseurDto::toEntity).toList())
                .dateCmde(cmdeFournisseurDto.getDateCmde())
                .fournisseur(FournisseurDto.toEntity(cmdeFournisseurDto.getFournisseurDto()))
                .idEntreprise(cmdeFournisseurDto.getEntrepriseId())
                .build();
    }

    public boolean isCommandeLivree() {
        return EtatCmde.LIVREE.equals(this.etatCommande);
    }

}

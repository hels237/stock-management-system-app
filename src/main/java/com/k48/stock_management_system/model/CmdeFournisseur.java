package com.k48.stock_management_system.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;


@SuperBuilder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CmdeFournisseur  extends AbstractEntity {

    private String code;

    private Instant dateCmde;

    private Integer idEntreprise;

    @Enumerated(EnumType.STRING)
    private EtatCmde etatCommande;

    @ManyToOne
    @JoinColumn(name = "fournisseurId")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "cmdeFournisseur")
    private List<LigneCmdeFournisseur> ligneCmdeFournisseur;
}

package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.Instant;
import java.util.List;


@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CmdeFournisseur  extends AbstractEntity {

    private String code;
    private Instant dateCmde;

    @ManyToOne
    @JoinColumn(name = "fournisseurId")
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "cmdeFournisseur")
    private List<LigneCmdeFournisseur> ligneCmdeFournisseur;
}

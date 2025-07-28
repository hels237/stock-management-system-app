package com.k48.stock_management_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;


@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vente extends AbstractEntity{

    private String code;

    private String commentaire;

    private Instant dateVente;

    private Integer idEntreprise;


    @OneToMany(mappedBy = "vente")
    private List<LigneVente> ligneVentes;
}

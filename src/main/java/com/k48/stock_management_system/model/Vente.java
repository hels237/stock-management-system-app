package com.k48.stock_management_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.Instant;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vente extends AbstractEntity{
    private String code;
    private String commentaire;
    private Instant dateVente;

    @OneToMany(mappedBy = "vente")
    private List<LigneVente> ligneVentes;
}

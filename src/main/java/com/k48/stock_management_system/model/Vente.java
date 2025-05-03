package com.k48.stock_management_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

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

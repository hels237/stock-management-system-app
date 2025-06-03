package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Fournisseur extends AbstractEntity {

    private String nom;

    private String prenom;

    private String email;

    private String photo;

    private String numTelephone;

    private Integer entrepriseId;

    @OneToMany(mappedBy = "fournisseur")
    private List<CmdeFournisseur> CmdeFournisseurs;
}

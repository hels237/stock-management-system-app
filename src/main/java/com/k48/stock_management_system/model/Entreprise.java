package com.k48.stock_management_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Entreprise extends AbstractEntity {

    private String nom;

    private String description;

    private Adresse adresse;

    private String codeFiscal;

    private String photo;

    private String email;

    private String numTelephone;

    private String siteWeb;

    private Integer entrepriseId;

    @OneToMany(mappedBy = "entreprise")
    private List<Utilisateur> utilisateurs;

    @OneToMany(mappedBy = "entreprise")
    private List<Article> articles;



}

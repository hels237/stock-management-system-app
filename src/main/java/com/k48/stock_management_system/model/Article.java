package com.k48.stock_management_system.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article extends AbstractEntity{

    private String codeArticle;

    private String designation;

    private BigDecimal prixUnitaireHT;

    private BigDecimal tauxTva;

    private BigDecimal prixUnitaireTTc;

    private Integer seuilMinimum;

    //private Integer idEntreprise;

    @Column(name = "urlPhoto")
    private String Photo;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "entrepriseId")
    private Entreprise entreprise;

    @OneToMany(mappedBy = "article")
    private List<LigneCmdeClient> ligneCmdeClients;

    @OneToMany(mappedBy = "article")
    private List<LigneCmdeFournisseur> ligneCmdeFournisseur;

    @OneToMany(mappedBy ="article")
    private List<MvtStock> mvtStock;

    @OneToMany(mappedBy = "article")
    private List<LigneVente> ligneVentes;


}

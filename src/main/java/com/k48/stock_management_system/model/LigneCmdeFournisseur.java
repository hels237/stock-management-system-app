package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LigneCmdeFournisseur extends AbstractEntity {

    private BigDecimal prixUnitaire;
    private BigDecimal quantite;

    @ManyToOne
    @JoinColumn(name = "cmdeFournisseurId")
    private CmdeFournisseur cmdeFournisseur;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

}

package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LigneCmdeClient extends AbstractEntity{

    private BigDecimal prixUnitaire;

    private BigDecimal quantite;

    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name = "cmdeClientId")
    private CmdeClient cmdeClient;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

}

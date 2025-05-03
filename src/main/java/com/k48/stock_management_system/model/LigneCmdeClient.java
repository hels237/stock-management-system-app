package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LigneCmdeClient extends AbstractEntity{

    private BigDecimal prixUnitaire;
    private BigDecimal quantite;

    @ManyToOne
    @JoinColumn(name = "cmdeClientId")
    private CmdeClient cmdeClient;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

}

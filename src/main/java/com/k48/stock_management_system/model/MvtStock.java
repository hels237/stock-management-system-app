package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;


@SuperBuilder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MvtStock extends AbstractEntity {

    private Instant dateMvt;

    private BigDecimal quantite;

    private Integer idEntreprise;

    @ManyToOne
    @JoinColumn(name = "mvtStockId")
    private Article article;;
}

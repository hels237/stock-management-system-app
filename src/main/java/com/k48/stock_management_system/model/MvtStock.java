package com.k48.stock_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MvtStock extends AbstractEntity {
    private Instant dateMvt;
    private BigDecimal quantite;

    @ManyToOne
    @JoinColumn(name = "mvtStockId")
    private Article article;;
}

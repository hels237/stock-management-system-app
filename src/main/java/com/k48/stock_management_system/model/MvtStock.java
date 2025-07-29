package com.k48.stock_management_system.model;

import jakarta.persistence.*;
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
    @JoinColumn(name = "articleId")
    private Article article;;

    @Enumerated(EnumType.STRING)
    private TypeMvtStock typeMvt;

    @Enumerated(EnumType.STRING)
    private SourceMvtStock sourceMvt;
}

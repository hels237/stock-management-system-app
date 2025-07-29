package com.k48.stock_management_system.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;


@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CmdeClient extends AbstractEntity {

    private String code;

    private Instant dateCmde;

    private Integer idEntreprise;

    @Enumerated(EnumType.STRING)
    private EtatCmde etatCommande;

    @ManyToOne
    @JoinColumn(name = "clientId")
    private Client client;

    @OneToMany(mappedBy = "cmdeClient")
    private List<LigneCmdeClient> ligneCmdeClients;
}

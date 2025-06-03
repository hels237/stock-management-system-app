package com.k48.stock_management_system.model;


import jakarta.persistence.Embeddable;
import lombok.*;


@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {

    private String adresse1;

    private String adresse2;

    private String ville;

    private String pays;

    private String codePostal;
}

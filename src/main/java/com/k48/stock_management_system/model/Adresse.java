package com.k48.stock_management_system.model;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
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

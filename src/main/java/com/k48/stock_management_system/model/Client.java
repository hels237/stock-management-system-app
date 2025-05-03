package com.k48.stock_management_system.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Client extends AbstractEntity {
    private String nom;
    private String prenom;
    private String email;
    @Column(name = "urlImage")
    private String photo;
    @Column(name = "telephone")
    private String numTelephone;

    @Embedded
    private Adresse adresse;

    @OneToMany(mappedBy = "client")
    private List<CmdeClient> cmdeClients;
}

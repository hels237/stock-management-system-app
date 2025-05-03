package com.k48.stock_management_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Utilisateur extends AbstractEntity {
    private String nom;
    private String prenom;
    private String email;
    private String photo;
    private String motDePasse;

    @ManyToOne
    @JoinColumn(name = "entrepriseId")
    private Entreprise entreprise;
}

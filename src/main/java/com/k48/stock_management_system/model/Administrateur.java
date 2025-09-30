package com.k48.stock_management_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("ADMIN")
@NoArgsConstructor
public class Administrateur extends Utilisateur{

}

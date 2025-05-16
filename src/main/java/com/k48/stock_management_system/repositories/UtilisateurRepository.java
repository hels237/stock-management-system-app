package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur ,Integer> {
}

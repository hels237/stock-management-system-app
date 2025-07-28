package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur ,Integer> {
    Optional<Utilisateur> findByEmail(String email);
}

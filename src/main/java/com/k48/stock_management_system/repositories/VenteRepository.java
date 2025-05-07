package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VenteRepository extends JpaRepository<Vente,Integer> {
    Optional<Vente> findByCode(String code);
}

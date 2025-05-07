package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntrepriseRepository extends JpaRepository<Entreprise,Integer> {

    Optional<Entreprise> findById(Integer id);
    List<Entreprise> findAll();
}

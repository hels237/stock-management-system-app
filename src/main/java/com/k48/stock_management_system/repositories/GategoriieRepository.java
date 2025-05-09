package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GategoriieRepository extends JpaRepository<Categorie,Integer> {

    Optional<Categorie> findByCode(String code);
}

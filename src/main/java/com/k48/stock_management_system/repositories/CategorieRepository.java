package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
}

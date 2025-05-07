package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LigneVenteRepository extends JpaRepository<LigneVente,Integer> {
    Optional<LigneVente> findLigneVenteByArticleId(Integer articleId);
}

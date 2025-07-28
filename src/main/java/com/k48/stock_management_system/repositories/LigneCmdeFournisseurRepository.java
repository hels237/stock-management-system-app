package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.LigneCmdeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneCmdeFournisseurRepository extends JpaRepository<LigneCmdeFournisseur,Integer> {
    List<LigneCmdeFournisseur> findAllByCmdeFournisseurId(Integer idCommande);

    List<LigneCmdeFournisseur> findAllByArticleId(Integer idCommande);
}

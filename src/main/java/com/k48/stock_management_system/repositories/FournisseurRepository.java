package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.CmdeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FournisseurRepository extends JpaRepository<CmdeFournisseur,Integer> {
}

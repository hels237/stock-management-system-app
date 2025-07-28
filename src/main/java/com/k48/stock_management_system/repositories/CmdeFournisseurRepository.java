package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.CmdeClient;
import com.k48.stock_management_system.model.CmdeFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CmdeFournisseurRepository extends JpaRepository<CmdeFournisseur,Integer> {
    Optional<CmdeFournisseur> findByCode(String code);
    List<CmdeFournisseur> findByFournisseurId(Integer id);
    List<CmdeClient> findAllByFournisseurId(Integer id);
}

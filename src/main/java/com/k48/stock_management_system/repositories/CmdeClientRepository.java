package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.CmdeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CmdeClientRepository extends JpaRepository<CmdeClient,Integer> {

    Optional<CmdeClient> findByClientId(Integer id);

    Optional<CmdeClient> findByCode(String codeCmdeClient);
}

package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.LigneCmdeClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LigneCmdeClientRepository extends JpaRepository<LigneCmdeClient,Integer> {
    Optional<LigneCmdeClient> findLigneCmdeClientByArticleId(Integer articleId);
    List<LigneCmdeClient> findAllByCmdeClientId(Integer id);

    List<LigneCmdeClient> findAllByArticleId(Integer id);
}

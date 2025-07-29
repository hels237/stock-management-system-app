package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.MvtStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MvtStockRepository extends JpaRepository<MvtStock,Integer> {
    @Query("select sum(m.quantite) from MvtStock m  where m.article.id = :idArticle")
    BigDecimal stockReelArticle(@Param("idArticle") Integer idArticle);

    List<MvtStock> findAllByArticleId(Integer idArticle);
}

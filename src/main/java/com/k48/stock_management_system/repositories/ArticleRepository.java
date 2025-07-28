package com.k48.stock_management_system.repositories;

import com.k48.stock_management_system.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface ArticleRepository extends JpaRepository<Article,Integer> {
    Optional<Article> findByCodeArticle(String codeArticle);
    List<Article> findAllByCategoryId(Integer idCategory);
    List<Article> findByDesignation(String designation);


}

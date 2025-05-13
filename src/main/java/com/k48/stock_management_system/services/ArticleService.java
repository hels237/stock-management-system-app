package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.ArticleDto;

import java.util.List;

public interface ArticleService extends AbstractService<ArticleDto> {

    ArticleDto save(ArticleDto articleDto);
    ArticleDto findByCodeArticle(String codeArticle);
    List<ArticleDto> findAllByDesignation(String designation);
    List<ArticleDto> findAll();

    void delete(Integer id);

}

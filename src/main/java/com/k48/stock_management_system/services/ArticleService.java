package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneVenteDto;

import java.util.List;


public interface ArticleService  {


    ArticleDto save(ArticleDto dto);

    ArticleDto findById(Integer id);

    ArticleDto findByCodeArticle(String codeArticle);

    List<ArticleDto> findAll();

    List<LigneVenteDto> findAllLigneVentes(Integer idArticle);

    List<LigneCmdeClientDto> findAllLigneCmdeClient(Integer idArticle);

    List<LigneCmdeFournisseurDto> findAllLigneCmdeFournisseur(Integer idArticle);

    List<ArticleDto> findAllArticleByCategoryId(Integer idCategory);

    ArticleDto delete(Integer id);

}

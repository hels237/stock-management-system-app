package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.dto.CategirieDto;
import com.k48.stock_management_system.model.Categorie;

import java.util.Optional;

public interface CategorieService extends AbstractService<CategirieDto> {
    public CategirieDto findByCode(String code);
}

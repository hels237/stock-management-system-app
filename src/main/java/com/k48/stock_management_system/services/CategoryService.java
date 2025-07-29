package com.k48.stock_management_system.services;

import com.k48.stock_management_system.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryDto dto);

    CategoryDto findById(Integer id);

    CategoryDto findByCode(String code);

    List<CategoryDto> findAll();

    CategoryDto delete(Integer id);
}

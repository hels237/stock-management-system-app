package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.CategoryDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.model.Category;
import com.k48.stock_management_system.repositories.CategoryRepository;
import com.k48.stock_management_system.services.CategoryService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final ObjectValidator<CategoryDto> objectValidator;
    private final CategoryRepository categoryRepository;


    @Override
    public CategoryDto save(CategoryDto categoryDto) {

        objectValidator.validate(categoryDto);
        return CategoryDto.toDto(categoryRepository.save(CategoryDto.toEntity(categoryDto)));
    }

    @Override
    public CategoryDto findById(Integer id) {
        if(id == null) {
            log.error("id is null");
        }

        return
                categoryRepository
                        .findById(id)
                        .map(CategoryDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("category with ID " + id + " not found"+ ErrorCode.CATEGORIE_NOT_FOUND)
                        );
    }


    @Override
    public List<CategoryDto> findAll() {

        List<Category> categories = categoryRepository.findAll();

        return
                Optional.of(categories)
                        .filter(list-> !list.isEmpty())
                        .orElseThrow(
                                ()-> new EntityNotFoundException(" EMPTY LIST OF CATEGORIES "+ ErrorCode.CATEGORIE_NOT_FOUND)
                        )
                        .stream()
                        .map(CategoryDto::toDto)
                        .toList();
    }

    @Override
    public CategoryDto delete(Integer id) {

        if(id == null) {
            log.error("0==) id: "+id +"is null");
        }
        Category category = categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("category with ID " + id + " not found"));
        categoryRepository.delete(category);
        return CategoryDto.toDto(category);
    }

    @Override
    public CategoryDto findByCode(String code) {
        if(code == null) {
            log.error("code is null !");
        }

        return
                categoryRepository
                        .findByCode(code)
                        .map(CategoryDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("category with id "+code+" not found")
                        );

    }
}

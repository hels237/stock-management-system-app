package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.CategoryDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.model.Category;
import com.k48.stock_management_system.repositories.ArticleRepository;
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
    private final ArticleRepository articleRepository;


    @Override
    public CategoryDto save(CategoryDto categoryDto) {

        objectValidator.validate(categoryDto);
        
        // Vérifier l'unicité par code
        if (categoryDto.getCode() != null) {
            Optional<Category> existingByCode = categoryRepository.findByCode(categoryDto.getCode());
            if (existingByCode.isPresent()) {
                log.error("Une catégorie avec le code {} existe déjà", categoryDto.getCode());
                throw new InvalidOperationException(
                    "Une catégorie avec ce code existe déjà", 
                    ErrorCode.CATEGORY_ALREADY_EXISTS
                );
            }
        }
        
        return CategoryDto.fromEntity(categoryRepository.save(CategoryDto.toEntity(categoryDto)));
    }

    @Override
    public CategoryDto findById(Integer id) {
        if(id == null) {
            log.error("category id is null");
        }

        return
                categoryRepository
                        .findById(id)
                        .map(CategoryDto::fromEntity)
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
                        .map(CategoryDto::fromEntity)
                        .toList();
    }

    @Override
    public CategoryDto delete(Integer id) {

        if(id == null) {
            log.error("0==) id: "+id +"is null");
        }
        Category category = categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("category with ID " + id + " not found"));
        List<Article> articles = articleRepository.findAllByCategoryId(id);
        if (!articles.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer cette categorie qui est deja utilise",
                    ErrorCode.CATEGORY_ALREADY_IN_USE);
        }
        categoryRepository.delete(category);
        return CategoryDto.fromEntity(category);
    }

    @Override
    public CategoryDto findByCode(String code) {
        if(code == null) {
            log.error("category code is null !");
        }

        return
                categoryRepository
                        .findByCode(code)
                        .map(CategoryDto::fromEntity)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("category with id "+code+" not found")
                        );

    }
}

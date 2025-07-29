package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.CategirieDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.model.Category;
import com.k48.stock_management_system.repositories.CategoryRepository;
import com.k48.stock_management_system.services.CategorieService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategorieServiceImpl implements CategorieService {

    private final ObjectValidator<CategirieDto> objectValidator;
    private final CategoryRepository categoryRepository;


    @Override
    public CategirieDto save(CategirieDto categirieDto) {

        objectValidator.validate(categirieDto);
        return CategirieDto.toDto(categoryRepository.save(CategirieDto.toEntity(categirieDto)));
    }

    @Override
    public CategirieDto findById(Integer id) {
        if(id == null) {
            log.error("id is null");
        }

        return
                categoryRepository
                        .findById(id)
                        .map(CategirieDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("category with ID " + id + " not found"+ ErrorCode.CATEGORIE_NOT_FOUND)
                        );
    }


    @Override
    public List<CategirieDto> findAll() {

        List<Category> categories = categoryRepository.findAll();

        return
                Optional.of(categories)
                        .filter(list-> !list.isEmpty())
                        .orElseThrow(
                                ()-> new EntityNotFoundException(" EMPTY LIST OF CATEGORIES "+ ErrorCode.CATEGORIE_NOT_FOUND)
                        )
                        .stream()
                        .map(CategirieDto::toDto)
                        .toList();
    }

    @Override
    public CategirieDto delete(Integer id) {

        if(id == null) {
            log.error("0==) id: "+id +"is null");
        }
        Category category = categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("category with ID " + id + " not found"));
        categoryRepository.delete(category);
        return CategirieDto.toDto(category);
    }

    @Override
    public CategirieDto findByCode(String code) {
        if(code == null) {
            log.error("code is null !");
        }

        return
                categoryRepository
                        .findByCode(code)
                        .map(CategirieDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("category with id "+code+" not found")
                        );

    }
}

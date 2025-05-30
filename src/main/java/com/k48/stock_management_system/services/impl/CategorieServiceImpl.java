package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.CategirieDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.model.Categorie;
import com.k48.stock_management_system.repositories.CategorieRepository;
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
    private final CategorieRepository  categorieRepository;


    @Override
    public CategirieDto save(CategirieDto categirieDto) {

        objectValidator.validate(categirieDto);
        return CategirieDto.toDto(categorieRepository.save(CategirieDto.toEntity(categirieDto)));
    }

    @Override
    public CategirieDto findById(Integer id) {
        if(id == null) {
            log.error("id is null");
        }

        return
                categorieRepository
                        .findById(id)
                        .map(CategirieDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("category with ID " + id + " not found"+ ErrorCode.CATEGORIE_NOT_FOUND)
                        );
    }


    @Override
    public List<CategirieDto> findAll() {

        List<Categorie> categories = categorieRepository.findAll();

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
        Categorie categorie = categorieRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("category with ID " + id + " not found"));
        categorieRepository.delete(categorie);
        return CategirieDto.toDto(categorie);
    }

    @Override
    public CategirieDto findByCode(String code) {
        if(code == null) {
            log.error("code is null !");
        }

        return
                categorieRepository
                        .findByCode(code)
                        .map(CategirieDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("category with id "+code+" not found")
                        );

    }
}

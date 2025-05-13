package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.services.ArticleService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public abstract class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final ObjectValidator<ArticleDto> objectValidator;




    @Override
    public ArticleDto save(ArticleDto articleDto) {
        //validate the object
        objectValidator.validate(articleDto);

        return ArticleDto.toDto(articleRepository.save(ArticleDto.toEntity(articleDto)));
    }

    @Override
    public ArticleDto findById(Integer id) {

        if(id == null) {
            return null;
        }
        return
                articleRepository
                        .findById(id)
                        .map(ArticleDto::toDto)
                        .orElseThrow(
                                ()->  new EntityNotFoundException("Article with id " + id + " not found")
                        ) ;

    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {

        if(codeArticle == null) {
            return null;
        }

        return
                articleRepository
                        .findByCodeArticle(codeArticle)
                        .map(ArticleDto::toDto)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Article with code " + codeArticle + " not found")
                        );
    }

    @Override
    public List<ArticleDto> findAllByDesignation(String designation) {

        List<Article> articles = articleRepository.findByDesignation(designation);

//Optionnal.of() permet d'eviter les li
        return
                Optional.of(articles)
                        .filter(list-> !list.isEmpty())
                        .orElseThrow(
                            ()-> new EntityNotFoundException("Aucun article trouvé avec la désignation: " + designation))
                        .stream()
                        .map(ArticleDto::toDto)
                        .toList();
    }


    @Override
    public List<ArticleDto> findAll() {

        List<Article> articles = articleRepository.findAll();

        return
                Optional.of(articles)
                        .filter(list-> !list.isEmpty())
                        .orElseThrow(
                                ()-> new EntityNotFoundException(" article not found ! ")
                        )
                        .stream()
                        .map(ArticleDto::toDto)
                        .toList();
    }



    @Override
    public void delete(Integer id) {

        if(id == null) {
            return ;
        }
        articleRepository.deleteById(id);

    }

}

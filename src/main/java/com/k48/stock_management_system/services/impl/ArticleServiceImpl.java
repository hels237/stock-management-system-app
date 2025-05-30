package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.services.ArticleService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
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
           log.error("--0=> id is null"+id+"= ?");
        }
        return
                articleRepository
                        .findById(id)
                        .map(ArticleDto::toDto)
                        .orElseThrow(
                                ()->  new EntityNotFoundException("Article with id " + id + " not found"    )
                        ) ;

    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {

        if(codeArticle == null) {
            log.error("--0=> codeArticle is null");
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
    public List<ArticleDto> findAll() {

        List<Article> articles = articleRepository.findAll();

        return
                Optional.of(articles)
                        .filter(list-> !list.isEmpty())
                        .orElseThrow(
                                ()-> new EntityNotFoundException(" EMPTY LIST ! "+ErrorCode.EMPTY_LIST)
                        )
                        .stream()
                        .map(ArticleDto::toDto)
                        .toList();
    }



    @Override
    public ArticleDto delete(Integer id) {

        if(id == null) {
            log.error("--0=> id is null");
        }
        Article article =
                articleRepository
                        .findById(id)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Article with id " + id + " not found"+ ErrorCode.ARTICLE_NOT_FOUND)
                        );
        articleRepository.delete(article);

        return ArticleDto.toDto(article);
    }

}

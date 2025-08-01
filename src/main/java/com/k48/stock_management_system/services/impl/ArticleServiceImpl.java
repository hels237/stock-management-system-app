package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneVenteDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.model.LigneCmdeClient;
import com.k48.stock_management_system.model.LigneCmdeFournisseur;
import com.k48.stock_management_system.model.LigneVente;
import com.k48.stock_management_system.repositories.ArticleRepository;
import com.k48.stock_management_system.repositories.LigneCmdeClientRepository;
import com.k48.stock_management_system.repositories.LigneCmdeFournisseurRepository;
import com.k48.stock_management_system.repositories.LigneVenteRepository;
import com.k48.stock_management_system.services.ArticleService;
import com.k48.stock_management_system.validator.ObjectValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public  class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final LigneVenteRepository venteRepository;
    private final LigneCmdeFournisseurRepository commandeFournisseurRepository;
    private final LigneCmdeClientRepository commandeClientRepository;

    private final ObjectValidator<ArticleDto> objectValidator;




    @Override
    public ArticleDto save(ArticleDto articleDto) {
        //validate the object
        objectValidator.validate(articleDto);

        return ArticleDto.fromEntity(articleRepository.save(ArticleDto.toEntity(articleDto)));
    }

    @Override
    public ArticleDto findById(Integer id) {

        if(id == null) {
           log.error("--0=> id is null"+id+"= ?");
        }
        return
                articleRepository
                        .findById(id)
                        .map(ArticleDto::fromEntity)
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
                        .map(ArticleDto::fromEntity)
                        .orElseThrow(
                                ()-> new EntityNotFoundException("Article with code " + codeArticle + " not found",ErrorCode.ARTICLE_NOT_FOUND)
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
                        .map(ArticleDto::fromEntity)
                        .toList();
    }

    @Override
    public List<LigneVenteDto> findAllLigneVentes(Integer idArticle){
        return venteRepository.findAllByArticleId(idArticle).stream()
                .map(LigneVenteDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCmdeClientDto> findAllLigneCmdeClient(Integer idArticle){
        return commandeClientRepository.findAllByArticleId(idArticle).stream()
                .map(LigneCmdeClientDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<LigneCmdeFournisseurDto> findAllLigneCmdeFournisseur(Integer idArticle){
        return commandeFournisseurRepository.findAllByArticleId(idArticle).stream()
                .map(LigneCmdeFournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDto> findAllArticleByCategoryId(Integer idCategory){
        return articleRepository.findAllByCategoryId(idCategory).stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
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


        List<LigneCmdeClient> ligneCommandeClients = commandeClientRepository.findAllByArticleId(id);
        if (!ligneCommandeClients.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes client", ErrorCode.ARTICLE_ALREADY_IN_USE);
        }
        List<LigneCmdeFournisseur> ligneCommandeFournisseurs = commandeFournisseurRepository.findAllByArticleId(id);
        if (!ligneCommandeFournisseurs.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des commandes fournisseur",
                    ErrorCode.ARTICLE_ALREADY_IN_USE);
        }
        List<LigneVente> ligneVentes = venteRepository.findAllByArticleId(id);
        if (!ligneVentes.isEmpty()) {
            throw new InvalidOperationException("Impossible de supprimer un article deja utilise dans des ventes",
                    ErrorCode.ARTICLE_ALREADY_IN_USE);
        }

        articleRepository.delete(article);
        return ArticleDto.fromEntity(article);
    }

}

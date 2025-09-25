package com.k48.stock_management_system.services.impl;


import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneVenteDto;
import com.k48.stock_management_system.exceptions.EntityNotFoundException;
import com.k48.stock_management_system.exceptions.ErrorCode;
import com.k48.stock_management_system.exceptions.InvalidOperationException;
import com.k48.stock_management_system.model.*;
import com.k48.stock_management_system.repositories.*;
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
    private final CategoryRepository categoryRepository;
    private final EntrepriseRepository entrepriseRepository;




    @Override
    public ArticleDto save(ArticleDto articleDto) {
        //valider l'object
        objectValidator.validate(articleDto);
        
        // Vérifier l'unicité du code article
        if (articleDto.getCodeArticle() != null) {
            Optional<Article> existingByCode = articleRepository.findByCodeArticle(articleDto.getCodeArticle());
            if (existingByCode.isPresent()) {
                log.error("Un article avec le code {} existe déjà", articleDto.getCodeArticle());
                throw new InvalidOperationException(
                    "Un article avec ce code existe déjà", 
                    ErrorCode.ARTICLE_ALREADY_EXISTS
                );
            }
        }

        Category category ;
        // Vérifier que la catégorie existe
        if (articleDto.getCategoryId() != null) {
            category = categoryRepository.findById(articleDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Catégorie avec l'ID " + articleDto.getCategoryId() + " non trouvée",
                    ErrorCode.CATEGORIE_NOT_FOUND
                ));
        } else {
            throw new InvalidOperationException(
                "La catégorie est obligatoire pour créer un article", 
                ErrorCode.CATEGORIE_NOT_FOUND
            );
        }
        
        Entreprise entreprise;
        // Vérifier que l'entreprise existe
        if (articleDto.getEntrepriseId() != null) {
            entreprise = entrepriseRepository.findById(articleDto.getEntrepriseId())
                .orElseThrow(() -> new EntityNotFoundException(
                    "Entreprise avec l'ID " + articleDto.getEntrepriseId() + " non trouvée",
                    ErrorCode.ENTREPPRISE_NOT_FOUND
                ));
        } else {
            throw new InvalidOperationException(
                "L'entreprise est obligatoire pour créer un article", 
                ErrorCode.ENTREPPRISE_NOT_FOUND
            );
        }
        Article article = Article.builder()
                                .codeArticle(articleDto.getCodeArticle())
                                .designation(articleDto.getDesignation())
                                .tauxTva(articleDto.getTauxTva())
                                .prixUnitaireHT(articleDto.getPrixUnitaireHT())
                                .prixUnitaireTTc(articleDto.getPrixUnitaireTTc())
                                .category(category)
                                .entreprise(entreprise)
                                .build();

        return ArticleDto.fromEntity(articleRepository.save(article));
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

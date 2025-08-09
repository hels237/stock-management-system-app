package com.k48.stock_management_system.controller;

import com.k48.stock_management_system.controller.api.ArticleApi;
import com.k48.stock_management_system.dto.ArticleDto;
import com.k48.stock_management_system.dto.LigneCmdeClientDto;
import com.k48.stock_management_system.dto.LigneCmdeFournisseurDto;
import com.k48.stock_management_system.dto.LigneVenteDto;
import com.k48.stock_management_system.services.ArticleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.k48.stock_management_system.utils.Constants.APP_ROOT;


@RestController
@Tag(name = "Articles", description = "API pour la gestion des articles")
@RequestMapping(APP_ROOT+"articles")
@RequiredArgsConstructor
public class ArticleController implements ArticleApi {

    private  ArticleService articleService;

    @Override
    public ArticleDto save(ArticleDto dto) {
        return  articleService.save(dto);
    }

    @Override
    public ArticleDto findById(Integer id) {
        return articleService.findById(id);
    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {
        return articleService.findByCodeArticle(codeArticle);
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleService.findAll();
    }

    @Override
    public List<LigneVenteDto> findHistoriqueVentes(Integer idArticle) {
        return articleService.findAllLigneVentes(idArticle);
    }

    @Override
    public List<LigneCmdeClientDto> findHistoriqueCommandeClient(Integer idArticle) {
        return articleService.findAllLigneCmdeClient(idArticle);
    }

    @Override
    public List<LigneCmdeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
        return articleService.findAllLigneCmdeFournisseur(idArticle);
    }

    @Override
    public List<ArticleDto> findAllArticleByIdCategory(Integer idCategory) {
        return List.of();
    }


    @Override
    public ArticleDto delete(Integer id) {
        return articleService.delete(id);
    }


}

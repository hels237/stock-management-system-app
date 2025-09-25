package com.k48.stock_management_system.dto;


import com.k48.stock_management_system.model.Article;
import com.k48.stock_management_system.model.Category;
import com.k48.stock_management_system.model.Entreprise;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class ArticleDto {

    private Integer id;

    @NotNull
    private String codeArticle;

    @NotNull
    private String designation;

    private BigDecimal prixUnitaireHT;

    private BigDecimal tauxTva;

    private BigDecimal prixUnitaireTTc;

    private Integer entrepriseId;

    private Integer categoryId;

    private String photo;

    private List<LigneCmdeClientDto> ligneCmdeClientDtos;

    private List<LigneCmdeFournisseurDto> ligneCmdeFournisseurDtos;

    private List<MvtStockDto> mvtStockDtos;



    public static ArticleDto fromEntity(Article article) {

        if(article == null){
            return null;
        }
        return ArticleDto
                .builder()
                .id(article.getId())
                .codeArticle(article.getCodeArticle())
                .designation(article.getDesignation())
                .prixUnitaireHT(article.getPrixUnitaireHT())
                .tauxTva(article.getTauxTva())
                .prixUnitaireTTc(article.getPrixUnitaireTTc())
                .entrepriseId(article.getEntreprise() != null ? article.getEntreprise().getId() : null)
                .categoryId(article.getCategory() != null ? article.getCategory().getId() : null)
                .build();
    }

    public static Article toEntity(ArticleDto  articleDto) {

        if(articleDto == null){
            return null;
        }

        return Article.
                builder()
                .id(articleDto.getId())
                .codeArticle(articleDto.getCodeArticle())
                .designation(articleDto.getDesignation())
                .prixUnitaireHT(articleDto.getPrixUnitaireHT())
                .prixUnitaireTTc(articleDto.getPrixUnitaireTTc())
                .tauxTva(articleDto.getTauxTva())
                .category(articleDto.getCategoryId() != null ?
                    Category.builder().id(articleDto.getCategoryId()).build() : null)
                .entreprise(articleDto.getEntrepriseId()!= null ?
                    Entreprise.builder().id(articleDto.getEntrepriseId()).build() : null)
                .build();
    }


}

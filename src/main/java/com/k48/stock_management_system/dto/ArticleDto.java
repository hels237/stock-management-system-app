package com.k48.stock_management_system.dto;


import com.k48.stock_management_system.model.Article;
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

    private String Photo;

    private Integer entrepriseId;

    //private Integer categoryId;

    private CategirieDto categirieDto;

    private EntrepriseDto entrepriseDto;

    private List<LigneCmdeClientDto> ligneCmdeClientDtos;

    private List<LigneCmdeFournisseurDto> ligneCmdeFournisseurDtos;

    private List<MvtStockDto> mvtStockDtos;



    public static Article toEntity(ArticleDto articleDto) {

        if(articleDto== null){
            return null;
        }
        return Article
                .builder()
                .codeArticle(articleDto.getCodeArticle())
                .designation(articleDto.getDesignation())
                .prixUnitaireHT(articleDto.getPrixUnitaireHT())
                .tauxTva(articleDto.getTauxTva())
                .prixUnitaireTTc(articleDto.getPrixUnitaireTTc())
                .categorie(CategirieDto.toEntity(articleDto.getCategirieDto()))
                .idEntreprise(articleDto.getEntrepriseId())
                .build();
    }

    public static ArticleDto toDto(Article  article) {

        if(article == null){
            return null;
        }

        return ArticleDto.
                builder()
                .codeArticle(article.getCodeArticle())
                .designation(article.getDesignation())
                .prixUnitaireHT(article.getPrixUnitaireHT())
                .prixUnitaireTTc(article.getPrixUnitaireTTc())
                .tauxTva(article.getTauxTva())
                .categirieDto(CategirieDto.toDto(article.getCategorie()))
                .entrepriseId(article.getIdEntreprise())
                .build();
    }


}

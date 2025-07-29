package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class CategirieDto {

    private Integer categorieId;

    @NotNull
    private String code;

    @NotNull
    private String designation;

    private String description;

    private Integer entrepriseId;

    private List<ArticleDto> articleDtos;



    public static Category toEntity(CategirieDto categirieDto) {

        if(categirieDto== null){
            return null;
        }
        return Category
                .builder()
                .code(categirieDto.getCode())
                .description(categirieDto.getDescription())
                .designation(categirieDto.getDesignation())
                .idEntreprise(categirieDto.getEntrepriseId())
                .articles(categirieDto.articleDtos.stream().map(ArticleDto::toEntity).toList())
                .build();
    }

    public static CategirieDto toDto(Category category) {

        if(category == null){
            return null;
        }
        return CategirieDto.
                builder()
                .code(category.getCode())
                .description(category.getDescription())
                .designation(category.getDesignation())
                .entrepriseId(category.getIdEntreprise())
                .articleDtos(category.getArticles().stream().map(ArticleDto::toDto).toList())
                .build();

    }

}

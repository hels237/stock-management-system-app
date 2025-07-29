package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class CategoryDto {

    private Integer categorieId;

    @NotNull
    private String code;

    @NotNull
    private String designation;

    private String description;

    private Integer entrepriseId;

    private List<ArticleDto> articleDtos;



    public static Category toEntity(CategoryDto categoryDto) {

        if(categoryDto == null){
            return null;
        }
        return Category
                .builder()
                .code(categoryDto.getCode())
                .description(categoryDto.getDescription())
                .designation(categoryDto.getDesignation())
                .idEntreprise(categoryDto.getEntrepriseId())
                .articles(categoryDto.articleDtos.stream().map(ArticleDto::toEntity).toList())
                .build();
    }

    public static CategoryDto toDto(Category category) {

        if(category == null){
            return null;
        }
        return CategoryDto.
                builder()
                .code(category.getCode())
                .description(category.getDescription())
                .designation(category.getDesignation())
                .entrepriseId(category.getIdEntreprise())
                .articleDtos(category.getArticles().stream().map(ArticleDto::toDto).toList())
                .build();

    }

}

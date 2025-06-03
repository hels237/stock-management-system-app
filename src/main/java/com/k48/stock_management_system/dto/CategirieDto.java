package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.Categorie;
import jakarta.validation.constraints.NotBlank;
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



    public static Categorie toEntity(CategirieDto categirieDto) {

        if(categirieDto== null){
            return null;
        }
        return Categorie
                .builder()
                .code(categirieDto.getCode())
                .description(categirieDto.getDescription())
                .designation(categirieDto.getDesignation())
                .articles(categirieDto.articleDtos.stream().map(ArticleDto::toEntity).toList())
                .build();
    }

    public static CategirieDto toDto(Categorie categorie) {

        if(categorie == null){
            return null;
        }
        return CategirieDto.
                builder()
                .code(categorie.getCode())
                .description(categorie.getDescription())
                .designation(categorie.getDesignation())
                .articleDtos(categorie.getArticles().stream().map(ArticleDto::toDto).toList())
                .build();

    }

}

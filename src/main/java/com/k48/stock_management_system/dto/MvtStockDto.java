package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.LigneCmdeFournisseur;
import com.k48.stock_management_system.model.MvtStock;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter @Setter
@Builder
public class MvtStockDto {

    private Instant dateMvt;

    private BigDecimal quantite;

    private ArticleDto articleDto;;


    public static MvtStockDto toDto(MvtStock mvtStock) {

        if(mvtStock == null) {
            return null;
        }
        return MvtStockDto
                .builder()
                .dateMvt(mvtStock.getDateMvt())
                .quantite(mvtStock.getQuantite())
                .articleDto(ArticleDto.toDto(mvtStock.getArticle()))
                .build();
    }

    public static MvtStock toEntity(MvtStockDto mvtStockDto) {

        if(mvtStockDto == null) {
            return null;
        }
        return MvtStock
                .builder()
                .quantite(mvtStockDto.getQuantite())
                .dateMvt(Instant.now())
                .article(ArticleDto.toEntity(mvtStockDto.getArticleDto()))
                .build();
    }
}

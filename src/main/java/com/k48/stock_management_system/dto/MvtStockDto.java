package com.k48.stock_management_system.dto;

import com.k48.stock_management_system.model.MvtStock;
import com.k48.stock_management_system.model.SourceMvtStock;
import com.k48.stock_management_system.model.TypeMvtStock;
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

    private ArticleDto articleDto;

    private TypeMvtStock typeMvt;

    private SourceMvtStock sourceMvt;

    private Integer entrepriseId;


    public static MvtStockDto fromEntity(MvtStock mvtStock) {

        if(mvtStock == null) {
            return null;
        }
        return MvtStockDto
                .builder()
                .dateMvt(mvtStock.getDateMvt())
                .typeMvt(mvtStock.getTypeMvt())
                .sourceMvt(mvtStock.getSourceMvt())
                .quantite(mvtStock.getQuantite())
                .articleDto(ArticleDto.fromEntity(mvtStock.getArticle()))
                .entrepriseId(mvtStock.getIdEntreprise())
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
                .typeMvt(mvtStockDto.getTypeMvt())
                .sourceMvt(mvtStockDto.getSourceMvt())
                .article(ArticleDto.toEntity(mvtStockDto.getArticleDto()))
                .idEntreprise(mvtStockDto.getEntrepriseId())
                .build();
    }
}
